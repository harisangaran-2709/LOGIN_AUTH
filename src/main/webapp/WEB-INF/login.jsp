<html>
<head>
    <title>Login</title>
</head>

<body>

    <h2>Login</h2>

    <p>${error}</p>

    <form action="${pageContext.request.contextPath}/login" method="post">

        <label>Email:</label>
        <input type="email" name="email" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <button type="submit">Login</button>

    </form>

    <p>Don't have an account?
        <a href="${pageContext.request.contextPath}/register">Register</a>
    </p>
</body>
</html>