<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Products</title>
</head>
<body>
<div class="d-flex justify-content-between align-items-center mb-4">
    <h2 class="mb-0">Danh sách sản phẩm</h2>
    <a class="btn btn-outline-secondary" href="${pageContext.request.contextPath}/home">Trang chủ</a>
</div>

<c:choose>
    <c:when test="${empty products}">
        <div class="alert alert-info">Chưa có sản phẩm.</div>
    </c:when>
    <c:otherwise>
        <div class="table-responsive">
            <table class="table table-bordered align-middle">
                <thead>
                <tr>
                    <th>Ảnh</th>
                    <th>Tên</th>
                    <th>Giá</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${products}" var="p">
                    <tr>
                        <td style="width:140px">
                            <a href="${pageContext.request.contextPath}/product-detail?id=${p.product_id}">
                                <c:choose>
                                    <c:when test="${not empty p.image}">
                                        <img src="${pageContext.request.contextPath}/uploads/products/${fn:escapeXml(p.image)}"
                                             width="110" height="110" style="object-fit:cover" alt="Ảnh sản phẩm">
                                    </c:when>
                                    <c:otherwise>Không có ảnh</c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                        <td><c:out value="${p.product_name}"/></td>
                        <td><c:out value="${p.price}"/> VNĐ</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<c:if test="${totalPage > 1}">
    <nav aria-label="Phân trang sản phẩm">
        <ul class="pagination">
            <c:forEach begin="1" end="${totalPage}" var="i">
                <li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/product?page=${i}"><c:out value="${i}"/></a>
                </li>
            </c:forEach>
        </ul>
    </nav>
</c:if>
</body>
</html>
