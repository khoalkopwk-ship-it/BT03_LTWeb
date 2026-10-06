<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Verify OTP</title>
</head>
<body>
<h2>Xác nhận OTP</h2>
<form action="${pageContext.request.contextPath}/verify-otp" method="post">
    <label for="otp">Nhập mã OTP:</label>
    <input id="otp" type="text" name="otp" inputmode="numeric"
           pattern="[0-9]{6}" minlength="6" maxlength="6" required autocomplete="one-time-code">
    <br><br>
    <button type="submit">Xác nhận</button>
</form>
<c:if test="${not empty message}">
    <p style="color:red"><c:out value="${message}"/></p>
</c:if>
</body>
</html>
