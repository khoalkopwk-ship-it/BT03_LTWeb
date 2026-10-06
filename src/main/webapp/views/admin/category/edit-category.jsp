<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!doctype html>
<html lang="vi">
<head>
    <title>Cập nhật Category</title>
</head>

<body>

<h2>Cập nhật Category</h2>

<c:if test="${not empty message}">
    <div class="alert alert-danger">
        <c:out value="${message}"/>
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/admin/category/edit"
      method="post"
      enctype="multipart/form-data"
      class="col-lg-6">

    <input type="hidden"
           name="csrfToken"
           value="${csrfToken}">

    <input type="hidden"
           name="cate_id"
           value="${category.cate_id}">

    <div class="mb-3">

        <label for="cate_name"
               class="form-label">
            Tên Category
        </label>

        <input id="cate_name"
               class="form-control"
               type="text"
               name="cate_name"
               value="${fn:escapeXml(category.cate_name)}"
               maxlength="255"
               required>

    </div>

    <c:if test="${not empty category.icons}">
        <div class="mb-3">

            <label class="form-label">
                Ảnh hiện tại
            </label>

            <br>

            <img
                    src="${pageContext.request.contextPath}/uploads/categories/${fn:escapeXml(category.icons)}"
                    width="120"
                    height="120"
                    style="object-fit: cover; border-radius: 8px;"
                    alt="Category">

        </div>
    </c:if>

    <div class="mb-3">

        <label for="icons"
               class="form-label">
            Chọn ảnh mới
        </label>

        <input id="icons"
               class="form-control"
               type="file"
               name="icons"
               accept="image/jpeg,image/png,image/webp,image/gif">

        <div class="form-text">
            Để trống nếu muốn giữ ảnh hiện tại.
        </div>

    </div>

    <button class="btn btn-primary"
            type="submit">
        Cập nhật
    </button>

    <a class="btn btn-secondary"
       href="${pageContext.request.contextPath}/admin/category">
        Hủy
    </a>

</form>

</body>
</html>