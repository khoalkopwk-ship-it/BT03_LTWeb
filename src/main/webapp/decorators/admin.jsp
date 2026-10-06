<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><sitemesh:write property="title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <sitemesh:write property="head"/>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <aside class="col-md-3 col-lg-2 bg-dark text-white min-vh-100 p-4">
            <h3 class="h4">Admin Panel</h3>
            <hr>
            <a href="${pageContext.request.contextPath}/admin/category"
               class="text-white text-decoration-none d-block mb-3">Quản lý Category</a>
            <a href="${pageContext.request.contextPath}/admin/product"
               class="text-white text-decoration-none d-block mb-3">Quản lý Product</a>
            <a href="${pageContext.request.contextPath}/home"
               class="text-white text-decoration-none d-block mb-3">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/logout"
               class="text-white text-decoration-none d-block">Logout</a>
        </aside>
        <main class="col-md-9 col-lg-10 p-4">
            <sitemesh:write property="body"/>
        </main>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
