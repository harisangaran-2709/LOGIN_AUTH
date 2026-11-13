package com.auth.servlet;

import com.auth.dao.UserDAO;
import com.auth.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if((email == null || email.trim().isEmpty()) || (password == null || password.isEmpty())) {
            request.setAttribute("error", "All fields are required.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
            dispatcher.forward(request, response);
            return;
        }

        email = email.trim();

        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.getUserByEmail(email);

            if(user != null) {
                boolean check = BCrypt.checkpw(password, user.getPassword());

                if(!check) {
                    LOGGER.warning("Invalid login attempt");
                    request.setAttribute("error", "Incorrect email or password.");
                    RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
                    dispatcher.forward(request, response);
                    return;
                }
                else {
                    LOGGER.info("User authenticated successfully");
                    HttpSession session = request.getSession();
                    request.changeSessionId();
                    session.setAttribute("userId", user.getId());
                    session.setAttribute("role", user.getRole());
                    response.sendRedirect(request.getContextPath() + "/dashboard");
                    return;
                }
            }
            LOGGER.warning("Invalid login attempt");
            request.setAttribute("error", "Incorrect email or password.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
            dispatcher.forward(request, response);
            return;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during login", e);
            request.setAttribute("error", "Something went wrong");
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/login.jsp");
            dispatcher.forward(request, response);
            return;
        }
    }
}
