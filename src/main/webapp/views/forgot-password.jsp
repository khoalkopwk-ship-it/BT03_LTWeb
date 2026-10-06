<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Forgot Password</title>
</head>
<body>
<h2>Quên mật khẩu</h2>
<form action="${pageContext.request.contextPath}/forgot-password" method="post">
    <label for="email">Email:</label>
    <input id="email" type="email" name="email" maxlength="255" required autocomplete="email">
    <br><br>
    <button type="submit">Gửi OTP</button>
</form>
<c:if test="${not empty message}">
    <p style="color:red"><c:out value="${message}"/></p>
</c:if>
<a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
</body>
</html>
