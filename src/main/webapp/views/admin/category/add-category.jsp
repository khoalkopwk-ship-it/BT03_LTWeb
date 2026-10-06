<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!doctype html>
<html lang="vi">
<head>
    <title>Thêm Category</title>
</head>

<body>

<h2>Thêm Category</h2>

<c:if test="${not empty message}">
    <div class="alert alert-danger">
        <c:out value="${message}"/>
    </div>
</c:if>

<form action="${pageContext.request.contextPath}/admin/category/add"
      method="post"
      enctype="multipart/form-data"
      class="col-lg-6">

    <input type="hidden"
           name="csrfToken"
           value="${csrfToken}">

    <div class="mb-3">
        <label for="cate_name"
               class="form-label">
            Tên Category
        </label>

        <input id="cate_name"
               class="form-control"
               type="text"
               name="cate_name"
               maxlength="255"
               required>
    </div>

    <div class="mb-3">
        <label for="icons"
               class="form-label">
            Ảnh Category
        </label>

        <input id="icons"
               class="form-control"
               type="file"
               name="icons"
               accept="image/jpeg,image/png,image/webp,image/gif">

        <div class="form-text">
            JPG, JPEG, PNG, WEBP hoặc GIF. Tối đa 5 MB.
        </div>
    </div>

    <button class="btn btn-primary"
            type="submit">
        Lưu
    </button>

    <a class="btn btn-secondary"
       href="${pageContext.request.contextPath}/admin/category">
        Hủy
    </a>

</form>

</body>
</html>