<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description = "Page Template" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@attribute name = "header" fragment = "true" %>
<%@attribute name = "footer" fragment = "true" %>
<%@attribute name = "menuBlock"%>
<%@attribute name = "menuRow"%>
<%@attribute name = "pageHeader"%>
<c:set var="role" value="${sessionScope.role}" scope="session"/>

<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/flatbootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootswatch.min.css">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/font-awesome.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-datetimepicker.min.css">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/flatselectize.bootstrap3.css">
    <jsp:invoke fragment="header" />
</head>
<html>
<body>
<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <a href="<%=request.getContextPath()%>/main/index" class="navbar-brand">Trips'n'Tickets</a>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
            <security:authorize access="hasRole('admin')">
            <ul class="nav navbar-nav">
                <li class="dropdown <c:if test="${menuBlock eq 'station'}">active</c:if>">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="stations">Станции<span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="stations">
                        <li <c:if test="${menuBlock eq 'station' && menuRow eq 'get'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/main/station/getStations">Список станций</a></li>
                        <li <c:if test="${menuBlock eq 'station' &&menuRow eq 'add'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/main/station/add">Добавить станцию</a></li>
                    </ul>
                </li>
                <li class="dropdown <c:if test="${menuBlock eq 'train'}">active</c:if>">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="trains">Поезда<span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="trains">
                        <li <c:if test="${menuBlock eq 'train' && menuRow eq 'get'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/main/train/getTrains">Список поездов</a></li>
                        <li <c:if test="${menuBlock eq 'train' && menuRow eq 'add'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/main/train/add">Добавить поезд</a></li>
                    </ul>
                </li>
                <li class="dropdown" <c:if test="${menuBlock eq 'route'}">active</c:if>>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="routes">Маршруты<span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="routes">
                        <li <c:if test="${menuBlock eq 'route' && menuRow eq 'get'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/secure/getRoutes">Список маршрутов</a></li>
                        <li <c:if test="${menuBlock eq 'route' && menuRow eq 'add'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/secure/addRoute">Добавить маршрут</a></li>
                    </ul>
                </li>
                <li class="dropdown" <c:if test="${menuBlock eq 'trip'}">active</c:if>>
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="trips">Рейсы<span class="caret"></span></a>
                    <ul class="dropdown-menu" aria-labelledby="trip">
                        <li <c:if test="${menuBlock eq 'trip' && menuRow eq 'get'}">class="active"</c:if>><a href="<%=request.getContextPath()%>/main/trip">Список рейсов</a></li>
                    </ul>
                </li>
            </ul>
            </security:authorize>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="<%=request.getContextPath()%>/main/search"><span class="glyphicon glyphicon-search"></span></a></li>
                <li><a href="<%=request.getContextPath()%>/main/index">На главную</a></li>
                <security:authorize access="isAuthenticated()">
                    <li><a href="<c:url value="/j_spring_security_logout"/>">Выйти</a></li>
                </security:authorize>
                <security:authorize access="isAnonymous()">
                    <li><a href="<%=request.getContextPath()%>/main/user/login">Войти</a></li>
                </security:authorize>
            </ul>
        </div>
    </div>
</div>

<div class="container">
    <div class="page-header">
        <div class="row">
            <div class="col-lg-8 col-md-7 col-sm-6">
                <h1>${pageHeader}</h1>
            </div>
        </div>
    </div>
    <jsp:doBody />
</div>
</div>
<script src="<%=request.getContextPath()%>/resources/js/jquery-1.11.0.js"></script>
<script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
<jsp:invoke fragment="footer" />
</body>
</html>