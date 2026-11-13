package com.auth.Tests;
import com.auth.dao.UserDAO;
import com.auth.model.User;
import com.auth.util.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConTest {
    public static void main(String[] args) throws SQLException {

        User user = new User("Hari", "hari@gmail.com", "hnnxh**etdja2456");
        UserDAO use = new UserDAO();
        boolean del = use.deleteUser(1);
    }
}
