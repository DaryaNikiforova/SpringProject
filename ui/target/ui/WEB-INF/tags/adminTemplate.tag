<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description = "Page Template" pageEncoding="UTF-8"%>
<%@attribute name = "header" fragment = "true" %>
<%@attribute name = "footer" fragment = "true" %>
<%@attribute name = "menuBlock"%>
<%@attribute name = "menuRow"%>
<%@attribute name = "pageHeader"%>
<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/metisMenu.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/sb-admin-2.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/font-awesome.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/bootstrap-datetimepicker.min.css" />
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/selectize.bootstrap3.css" />
    <jsp:invoke fragment="header" />
</head>
<html>
<body>
    <div id="wrapper">
        <div class="navbar-default sidebar" role="navigation">
            <div class="sidebar-nav navbar-collapse">
                <ul class="nav" id="side-menu">
                    <li <c:if test="${menuBlock eq 'train'}">class="active"</c:if>>
                        <a href="#"><i class="fa fa-fw"></i>Поезда<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="<%=request.getContextPath()%>/secure/getTrains" <c:if test="${menuRow eq 'get'}">class="active"</c:if>>Список поездов</a></li>
                            <li><a href="<%=request.getContextPath()%>/secure/addTrain" <c:if test="${menuRow eq 'add'}">class="active"</c:if>>Добавить поезд</a></li>
                        </ul>
                    </li>
                    <li <c:if test="${menuBlock eq 'station'}">class="active"</c:if>>
                        <a href="#"><i class="fa fa-fw"></i>Станции<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="<%=request.getContextPath()%>/secure/getStations" <c:if test="${menuRow eq 'get'}">class="active"</c:if>>Список станций</a></li>
                            <li><a href="<%=request.getContextPath()%>/secure/addStation" <c:if test="${menuRow eq 'add'}">class="active"</c:if>>Добавить станцию</a></li>
                        </ul>
                    </li>
                    <li <c:if test="${menuBlock eq 'route'}">class="active"</c:if>>
                        <a href="#"><i class="fa fa-fw"></i>Маршруты<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="<%=request.getContextPath()%>/secure/getRoutes" <c:if test="${menuRow eq 'get'}">class="active"</c:if>>Список маршрутов</a></li>
                            <li><a href="<%=request.getContextPath()%>/secure/addRoute" <c:if test="${menuRow eq 'add'}">class="active"</c:if>>Добавить маршрут</a></li>
                        </ul>
                    </li>
                    <li <c:if test="${menuBlock eq 'trip'}">class="active"</c:if>>
                        <a href="#"><i class="fa fa-fw"></i>Рейсы<span class="fa arrow"></span></a>
                        <ul class="nav nav-second-level">
                            <li><a href="<%=request.getContextPath()%>/secure/getTrips" <c:if test="${menuRow eq 'get'}">class="active"</c:if>>Список рейсов</a></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>

        <div id="page-wrapper">
            <div class="row">
                <div class="col-md-12">
                    <h1 class="page-header">${pageHeader}</h1>
                </div>
            </div>
            <jsp:doBody />
        </div>
    </div>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-1.11.0.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/bootstrap.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/metisMenu.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/sb-admin-2.js"></script>
    <jsp:invoke fragment="footer" />
</body>
</html>