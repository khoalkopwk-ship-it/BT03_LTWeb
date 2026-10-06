<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html lang="vi">
<head>
    <title>Quản lý Category</title>
</head>
<body>
<h2>Quản lý Category</h2>

<c:if test="${not empty flashMessage}">
    <div class="alert ${flashType == 'error' ? 'alert-danger' : 'alert-success'}" role="alert">
        <c:out value="${flashMessage}"/>
    </div>
</c:if>

<a class="btn btn-primary mb-3" href="${pageContext.request.contextPath}/admin/category/add">Thêm Category</a>

<div class="table-responsive">
<table class="table table-bordered table-striped align-middle">
    <thead>
    <tr>
        <th>ID</th>
        <th>Tên Category</th>
        <th>Icon</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${categories}" var="c">
        <tr>
            <td><c:out value="${c.cate_id}"/></td>
            <td><c:out value="${c.cate_name}"/></td>
            <td>
                <c:choose>
                    <c:when test="${not empty c.icons}">
                        <img
                                src="${pageContext.request.contextPath}/uploads/categories/${fn:escapeXml(c.icons)}"
                                alt="${fn:escapeXml(c.cate_name)}"
                                width="80"
                                height="80"
                                style="object-fit: cover; border-radius: 8px;"
                        >
                    </c:when>

                    <c:otherwise>
                        <span class="text-muted">Không có ảnh</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td class="d-flex gap-2">
                <a class="btn btn-sm btn-warning"
                   href="${pageContext.request.contextPath}/admin/category/edit?id=${c.cate_id}">Sửa</a>
                <form action="${pageContext.request.contextPath}/admin/category/delete" method="post"
                      onsubmit="return confirm('Bạn có chắc muốn xóa category này?')">
                    <input type="hidden" name="id" value="${c.cate_id}">
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
