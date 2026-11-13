package com.auth.servlet;

import com.auth.dao.UserDAO;
import com.auth.model.User;
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

@WebServlet("/users")
public class UserManagementServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(UserManagementServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        String action = request.getParameter("action");
        if (action == null || !"view".equals(action)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if ("view".equals(action)) {
            String idParam = request.getParameter("id");
            try {
                int id = Integer.parseInt(idParam);
                if (id <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserById(id);
                if (user == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                Integer loggedInUserId = (Integer) session.getAttribute("userId");
                if (!"ADMIN".equals(role) && loggedInUserId != id) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().println("Name: " + user.getName());
                response.getWriter().println("Email: " + user.getEmail());

            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error while fetching user", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String role = (String) session.getAttribute("role");
        String action = request.getParameter("action");
        if (action == null || (!"delete".equals(action))) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if ("delete".equals(action) && !"ADMIN".equals(role)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            try {
                int id = Integer.parseInt(idParam);
                if (id <= 0) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }
                Integer loggedInUserId = (Integer) session.getAttribute("userId");
                if (loggedInUserId == id) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }

                UserDAO userDAO = new UserDAO();
                boolean deleted = userDAO.deleteUser(id);
                if (!deleted) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error while deleting user", e);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }

        }
    }
}
