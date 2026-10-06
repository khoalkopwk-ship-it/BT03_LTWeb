<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head><title>Quản lý sản phẩm</title></head>
<body>
<h2>Quản lý sản phẩm</h2>

<c:if test="${not empty flashMessage}">
    <div class="alert ${flashType == 'error' ? 'alert-danger' : 'alert-success'}" role="alert">
        <c:out value="${flashMessage}"/>
    </div>
</c:if>

<a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/admin/product/add">Thêm sản phẩm</a>

<div class="table-responsive">
<table class="table table-bordered table-striped align-middle">
    <thead>
    <tr>
        <th>ID</th>
        <th>Ảnh</th>
        <th>Tên sản phẩm</th>
        <th>Giá</th>
        <th>Category</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${products}" var="p">
        <tr>
            <td><c:out value="${p.product_id}"/></td>
            <td>
                <c:choose>
                    <c:when test="${not empty p.image}">
                        <img src="${pageContext.request.contextPath}/uploads/products/${fn:escapeXml(p.image)}"
                             width="100" height="100" style="object-fit:cover" alt="Ảnh sản phẩm">
                    </c:when>
                    <c:otherwise>Không có ảnh</c:otherwise>
                </c:choose>
            </td>
            <td><c:out value="${p.product_name}"/></td>
            <td><c:out value="${p.price}"/></td>
            <td><c:out value="${p.category.cate_name}"/></td>
            <td class="d-flex gap-2">
                <a class="btn btn-sm btn-warning"
                   href="${pageContext.request.contextPath}/admin/product/edit?id=${p.product_id}">Sửa</a>
                <form action="${pageContext.request.contextPath}/admin/product/delete" method="post"
                      onsubmit="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                    <input type="hidden" name="id" value="${p.product_id}">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button class="btn btn-sm btn-danger" type="submit">Xóa</button>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
</body>
</html>
