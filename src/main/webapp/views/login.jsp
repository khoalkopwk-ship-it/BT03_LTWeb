<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login</title>
</head>
<body>
<h2>Đăng nhập</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    <label for="username">Username:</label>
    <input id="username" type="text" name="username"
           value="${fn:escapeXml(cookie.username.value)}"
           maxlength="100" required autocomplete="username">
    <br><br>

    <label for="password">Password:</label>
    <input id="password" type="password" name="password"
           required autocomplete="current-password">
    <br><br>

    <label>
        <input type="checkbox" name="remember"
               <c:if test="${not empty cookie.username.value}">checked</c:if>>
        Remember username
    </label>
    <br><br>

    <button type="submit">Login</button>
</form>

<c:if test="${not empty message}">
    <p style="color:red"><c:out value="${message}"/></p>
</c:if>

<a href="${pageContext.request.contextPath}/register">Đăng ký tài khoản</a>
<br>
<a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu</a>
</body>
</html>
