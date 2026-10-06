<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Chi tiết sản phẩm</title>
</head>
<body>
<a class="btn btn-outline-secondary mb-3" href="${pageContext.request.contextPath}/product">← Danh sách sản phẩm</a>
<div class="row g-4">
    <div class="col-md-5">
        <c:choose>
            <c:when test="${not empty product.image}">
                <img class="img-fluid rounded border"
                     src="${pageContext.request.contextPath}/uploads/products/${fn:escapeXml(product.image)}"
                     alt="Ảnh sản phẩm">
            </c:when>
            <c:otherwise>
                <div class="bg-secondary-subtle d-flex align-items-center justify-content-center rounded"
                     style="height:320px">Không có ảnh</div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="col-md-7">
        <h2><c:out value="${product.product_name}"/></h2>
        <h3 class="h4">Giá: <c:out value="${product.price}"/> VNĐ</h3>
        <p><strong>Mô tả:</strong></p>
        <p style="white-space:pre-wrap"><c:out value="${product.description}"/></p>
        <p><strong>Danh mục:</strong> <c:out value="${product.category.cate_name}"/></p>
    </div>
</div>
</body>
</html>
