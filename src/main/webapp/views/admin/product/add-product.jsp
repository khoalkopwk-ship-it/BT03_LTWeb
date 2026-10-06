<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head><title>Thêm sản phẩm</title></head>
<body>
<h2>Thêm sản phẩm</h2>
<c:if test="${not empty message}">
    <div class="alert alert-danger"><c:out value="${message}"/></div>
</c:if>
<form action="${pageContext.request.contextPath}/admin/product/add"
      method="post" enctype="multipart/form-data" class="col-lg-7">
    <input type="hidden" name="csrfToken" value="${csrfToken}">

    <div class="mb-3">
        <label for="product_name" class="form-label">Tên sản phẩm</label>
        <input id="product_name" class="form-control" type="text" name="product_name"
               value="${fn:escapeXml(param.product_name)}" maxlength="255" required>
    </div>

    <div class="mb-3">
        <label for="price" class="form-label">Giá</label>
        <input id="price" class="form-control" type="number" name="price"
               step="0.01" min="0.01" value="${fn:escapeXml(param.price)}" required>
    </div>

    <div class="mb-3">
        <label for="description" class="form-label">Mô tả</label>
        <textarea id="description" class="form-control" name="description"><c:out value="${param.description}"/></textarea>
    </div>

    <div class="mb-3">
        <label for="cate_id" class="form-label">Category</label>
        <select id="cate_id" class="form-select" name="cate_id" required>
            <c:forEach items="${categories}" var="c">
                <option value="${c.cate_id}" <c:if test="${param.cate_id == c.cate_id}">selected</c:if>>
                    <c:out value="${c.cate_name}"/>
                </option>
            </c:forEach>
        </select>
    </div>

    <div class="mb-3">
        <label for="image" class="form-label">Ảnh</label>
        <input id="image" class="form-control" type="file" name="image"
               accept="image/jpeg,image/png,image/webp,image/gif">
        <div class="form-text">Tối đa 5 MB. JPG/JPEG/PNG/WEBP/GIF.</div>
    </div>

    <button class="btn btn-primary" type="submit">Lưu</button>
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/product">Hủy</a>
</form>
</body>
</html>
