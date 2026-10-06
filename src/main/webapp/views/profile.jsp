<%@ page contentType="text/html;charset=UTF-8" %>

<html>

<head>

  <title>
    Profile
  </title>

</head>


<body>


<h2>
  Thông tin cá nhân
</h2>


<form action="${pageContext.request.contextPath}/profile"
      method="post"
      enctype="multipart/form-data">


  <div>

    <label>
      Username
    </label>

    <input value="${user.username}"
           readonly>

  </div>



  <div>

    <label>
      Email
    </label>

    <input value="${user.email}"
           readonly>

  </div>



  <div>

    <label>
      Fullname
    </label>

    <input name="fullname"
           value="${user.fullname}">

  </div>



  <div>

    <label>
      Phone
    </label>

    <input name="phone"
           value="${user.phone}">

  </div>



  <div>

    <label>
      Avatar
    </label>

    <input type="file"
           name="avatar">

  </div>



  <c:if test="${not empty user.avatar}">

    <img
            src="${pageContext.request.contextPath}/uploads/avatar/${user.avatar}"
            width="120">

  </c:if>



  <br>


  <button>
    Cập nhật
  </button>



</form>


</body>

</html>