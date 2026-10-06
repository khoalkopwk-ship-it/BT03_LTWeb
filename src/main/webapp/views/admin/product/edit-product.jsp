<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head><title>Cập nhật sản phẩm</title></head>
<body>
<h2>Cập nhật sản phẩm</h2>
<c:if test="${not empty message}">
    <div class="alert alert-danger"><c:out value="${message}"/></div>
</c:if>
<form action="${pageContext.request.contextPath}/admin/product/edit"
      method="post" enctype="multipart/form-data" class="col-lg-7">
    <input type="hidden" name="csrfToken" value="${csrfToken}">
    <input type="hidden" name="product_id" value="${product.product_id}">

    <div class="mb-3">
        <label for="product_name" class="form-label">Tên sản phẩm</label>
        <input id="product_name" class="form-control" type="text" name="product_name"
               value="${fn:escapeXml(not empty param.product_name ? param.product_name : product.product_name)}"
               maxlength="255" required>
    </div>

    <div class="mb-3">
        <label for="price" class="form-label">Giá</label>
        <input id="price" class="form-control" type="number" name="price" step="0.01" min="0.01"
               value="${fn:escapeXml(not empty param.price ? param.price : product.price)}" required>
    </div>

    <div class="mb-3">
        <label for="description" class="form-label">Mô tả</label>
        <textarea id="description" class="form-control" name="description"><c:out value="${not empty param.description ? param.description : product.description}"/></textarea>
    </div>

    <div class="mb-3">
        <label for="cate_id" class="form-label">Category</label>
        <select id="cate_id" class="form-select" name="cate_id" required>
            <c:forEach items="${categories}" var="c">
                <option value="${c.cate_id}"
                    <c:if test="${(not empty param.cate_id and param.cate_id == c.cate_id) or (empty param.cate_id and c.cate_id == product.category.cate_id)}">selected</c:if>>
                    <c:out value="${c.cate_name}"/>
                </option>
            </c:forEach>
        </select>
    </div>

    <c:if test="${not empty product.image}">
        <div class="mb-3">
            <div class="form-label">Ảnh hiện tại</div>
            <img src="${pageContext.request.contextPath}/uploads/products/${fn:escapeXml(product.image)}"
                 width="120" height="120" style="object-fit:cover" alt="Ảnh hiện tại">
        </div>
    </c:if>

    <div class="mb-3">
        <label for="image" class="form-label">Chọn ảnh mới</label>
        <input id="image" class="form-control" type="file" name="image"
               accept="image/jpeg,image/png,image/webp,image/gif">
        <div class="form-text">Để trống nếu giữ ảnh cũ. Tối đa 5 MB.</div>
    </div>

    <button class="btn btn-primary" type="submit">Cập nhật</button>
    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/admin/product">Hủy</a>
</form>
</body>
</html>
