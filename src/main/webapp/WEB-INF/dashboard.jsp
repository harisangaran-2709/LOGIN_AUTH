
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Dashboard</title>
</head>

<body>

    <h2>Dashboard</h2>

    <p><c:out value="${error}" /></p>

    <h1>Welcome <c:out value="${user.name}" /></h1>

    <h1>Email: <c:out value="${user.email}" /></h1>

    <h3>Role: <c:out value="${user.role}" /></h3>

    <!-- Available to all logged-in users -->
    <h3>My Profile</h3>

    <a href="${pageContext.request.contextPath}/users?action=view&id=${user.id}">
        View My Details
    </a>

    <!-- Available only to ADMIN -->
    <c:if test="${user.role == 'ADMIN'}">
        <hr>

        <h2>Admin Panel</h2>

        <a href="${pageContext.request.contextPath}/admin/users">
            Manage Users
        </a>
    </c:if>

    <hr>

    <form action="${pageContext.request.contextPath}/logout" method="post">
        <button type="submit">Logout</button>
    </form>

</body>
</html>