<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head><title>Home</title></head>
<body>
<h2>Xin chào: <c:out value="${sessionScope.account.fullname}"/></h2>
<p>Username: <c:out value="${sessionScope.account.username}"/></p>
<p>Email: <c:out value="${sessionScope.account.email}"/></p>
</body>
</html>
