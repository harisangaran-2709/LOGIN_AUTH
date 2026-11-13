
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Admin - Manage Users</title>
</head>

<body>

    <h1>Admin User Management</h1>

    <h2>Registered Users</h2>

    <table border="1" cellpadding="10" cellspacing="0">

        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Role</th>
                <th>Action</th>
            </tr>
        </thead>

        <tbody>
            <c:forEach var="user" items="${users}">
                <tr>
                    <td><c:out value="${user.id}" /></td>
                    <td><c:out value="${user.name}" /></td>
                    <td><c:out value="${user.email}" /></td>
                    <td><c:out value="${user.role}" /></td>

                    <td>
                        <c:if test="${user.id != sessionScope.userId}">
                            <form action="${pageContext.request.contextPath}/users"
                                  method="post">

                                <input type="hidden" name="action" value="delete">

                                <input type="hidden" name="id"
                                       value="${user.id}">

                                <button type="submit">Delete</button>

                            </form>
                        </c:if>
                    </td>
                </tr>
            </c:forEach>
        </tbody>

    </table>

    <br>

    <a href="${pageContext.request.contextPath}/dashboard">
        Back to Dashboard
    </a>

</body>
</html>
