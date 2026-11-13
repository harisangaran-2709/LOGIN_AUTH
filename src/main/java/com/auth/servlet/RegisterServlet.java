package com.auth.servlet;

import com.auth.dao.UserDAO;
import com.auth.model.User;
import org.mindrot.jbcrypt.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if((name == null || name.trim().isEmpty()) || (email == null || email.trim().isEmpty()) || (password == null || password.isEmpty())) {
            request.setAttribute("error", "All fields are required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
        name = name.trim();
        email = email.trim();

        String salt = BCrypt.gensalt();
        String hashedPassword =BCrypt.hashpw(password, salt);
        User user = new User(name, email, hashedPassword);
        UserDAO userDAO = new UserDAO();
        try {
            User user1 = userDAO.getUserByEmail(email);
            if(user1 != null) {
                request.setAttribute("error", "An account with this email already exists.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
                dispatcher.forward(request, response);
                return;
            }
            if(!userDAO.createUser(user)) {
                request.setAttribute("error", "Internal server error.");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
                dispatcher.forward(request, response);
                return;
            }
            response.sendRedirect(request.getContextPath() + "/login");
            return;

        } catch (SQLException e) {
            request.setAttribute("error", "Internal server error.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/register.jsp");
        dispatcher.forward(request, response);
    }
}
