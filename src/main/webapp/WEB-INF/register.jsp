<html>
<head>
    <title>Registration</title>
</head>

<body>

    <h2>Create Account</h2>

    <p>${error}</p>

    <form action="${pageContext.request.contextPath}/register" method="post">

        <label>Name:</label>
        <input type="text" name="name" required>

        <label>Email:</label>
        <input type="email" name="email" required>

        <label>Password:</label>
        <input type="password" name="password" required>

        <button type="submit">Register</button>

    </form>

    <p>Already have an account?
        <a href="${pageContext.request.contextPath}/login">Login</a>
    </p>

</body>
</html>