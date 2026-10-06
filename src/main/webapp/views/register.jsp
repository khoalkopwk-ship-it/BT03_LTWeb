<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register</title>
</head>
<body>
<h2>Đăng ký tài khoản</h2>
<form action="${pageContext.request.contextPath}/register" method="post">
    <label for="email">Email:</label>
    <input id="email" type="email" name="email"
           value="${fn:escapeXml(email)}" maxlength="255" required autocomplete="email">
    <br><br>

    <label for="username">Username:</label>
    <input id="username" type="text" name="username"
           value="${fn:escapeXml(username)}" minlength="3" maxlength="100" required autocomplete="username">
    <br><br>

    <label for="fullname">Fullname:</label>
    <input id="fullname" type="text" name="fullname"
           value="${fn:escapeXml(fullname)}" maxlength="255" required autocomplete="name">
    <br><br>

    <label for="password">Password:</label>
    <input id="password" type="password" name="password"
           minlength="6" maxlength="72" required autocomplete="new-password">
    <br><br>

    <button type="submit">Register</button>
</form>

<c:if test="${not empty message}">
    <p style="color:red"><c:out value="${message}"/></p>
</c:if>

<a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
</body>
</html>
