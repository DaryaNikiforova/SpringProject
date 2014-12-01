<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description = "Page Template" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@attribute name = "header" fragment = "true" %>
<%@attribute name = "footer" fragment = "true" %>
<%@attribute name = "page"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/font-awesome.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-datetimepicker.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/magister.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/open-sans.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/wire-one.css" />
    <jsp:invoke fragment="header" />
</head>
<html>
<body class="text-shadows">
    <nav class="mainmenu">
        <div class="container">
            <div class="dropdown open">
                <ul class="dropdown-menu pull-right" role="menu" aria-labelledby="dLabel">
                    <c:if test="${page != 'index'}">
                        <li><a href="<%=request.getContextPath()%>/main/index">На главную</a></li>
                    </c:if>
                    <security:authorize access="isAuthenticated()">
                        <security:authorize access="hasRole('admin')">
                            <li><a href="<%=request.getContextPath()%>/main/station/">Администрирование</a></li>
                        </security:authorize>
                            <li><a href="<%=request.getContextPath()%>/main/cabinet/">Личный кабинет</a></li>
                            <li><a href="<c:url value="/j_spring_security_logout"/>">Выйти</a></li>
                    </security:authorize>
                    <security:authorize access="isAnonymous()">
                        <li><a href="<%=request.getContextPath()%>/main/user/registration" <c:if test="${page eq 'registration'}">class="active"</c:if>>Регистрация</a></li>
                        <li><a href="<%=request.getContextPath()%>/main/user/login" <c:if test="${page eq 'login'}">class="active"</c:if>>Войти</a></li>
                    </security:authorize>
                </ul>
            </div>
        </div>
    </nav>

    <section class="section">
        <div class="container">
            <jsp:doBody />
        </div>
    </section>
     <script src="<%=request.getContextPath()%>/resources/js/jquery-1.11.0.js"></script>
     <script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
     <jsp:invoke fragment="footer" />
</body>
</html>