package com.auth.servlet;

import com.auth.dao.UserDAO;
import com.auth.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/admin/users")
public class AdminUsersServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AdminUsersServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserDAO userDAO = new UserDAO();

        try {
            List<User> users = userDAO.getAllUsers();

            request.setAttribute("users", users);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/admin-users.jsp");

            dispatcher.forward(request, response);

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while fetching all users", e);

            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}