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
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DashboardServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer uId = (Integer) session.getAttribute("userId");

        UserDAO userDAO = new UserDAO();

        try {
            User user = userDAO.getUserById(uId);

            if (user != null) {
                LOGGER.log(Level.INFO, "User Found");

                request.setAttribute("user", user);

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/dashboard.jsp");

                dispatcher.forward(request, response);
                return;
            }

            LOGGER.log(Level.WARNING, "User Not Found");
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/login");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error while loading dashboard", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}