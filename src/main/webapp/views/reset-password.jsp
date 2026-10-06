<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Reset Password</title>
</head>
<body>
<h2>Nhập mật khẩu mới</h2>
<form action="${pageContext.request.contextPath}/reset-password" method="post">
    <label for="password">Password mới:</label>
    <input id="password" type="password" name="password"
           minlength="6" maxlength="72" required autocomplete="new-password">
    <br><br>
    <button type="submit">Lưu mật khẩu</button>
</form>
<c:if test="${not empty message}">
    <p style="color:red"><c:out value="${message}"/></p>
</c:if>
</body>
</html>
