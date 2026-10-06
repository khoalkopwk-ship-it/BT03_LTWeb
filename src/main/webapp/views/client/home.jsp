<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Trang chủ</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Sản phẩm mới nhất</h2>
    <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/product">Xem tất cả sản phẩm</a>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="alert alert-info">Chưa có sản phẩm.</div>
    </c:when>
    <c:otherwise>
        <div class="row g-4">
            <c:forEach items="${products}" var="p">
                <div class="col-sm-6 col-lg-4">
                    <div class="card h-100 shadow-sm">
                        <c:choose>
                            <c:when test="${not empty p.image}">
                                <img class="card-img-top"
                                     src="${pageContext.request.contextPath}/uploads/products/${fn:escapeXml(p.image)}"
                                     style="height:260px;object-fit:cover" alt="Ảnh sản phẩm">
                            </c:when>
                            <c:otherwise>
                                <div class="bg-secondary-subtle d-flex align-items-center justify-content-center"
                                     style="height:260px">Không có ảnh</div>
                            </c:otherwise>
                        </c:choose>
                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title"><c:out value="${p.product_name}"/></h5>
                            <p class="card-text fw-semibold"><c:out value="${p.price}"/> VNĐ</p>
                            <a class="btn btn-primary mt-auto"
                               href="${pageContext.request.contextPath}/product-detail?id=${p.product_id}">Chi tiết</a>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>
</body>
</html>
