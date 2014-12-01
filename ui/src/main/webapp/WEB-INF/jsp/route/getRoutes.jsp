<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 19.10.14
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:flatTemplate menuBlock="route" menuRow="get" pageHeader="Маршруты">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/ajax-delete-items.js"></script>
        <script type="text/javascript">
            $(function () {
                ajaxHelper.setDeleteLinks('${pageContext.request.contextPath}/main/route/delete');
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <div class="col-md-8">
                <c:choose>
                <c:when test="${routes == null || routes.isEmpty()}">На данный момент никаких рейсов не задано</c:when>
                <c:otherwise>
                <div class="js-alerts">
                <c:if test="${errors != null && !errors.isEmpty()}">
                    <div class="alert alert-danger">
                        <c:forEach items="${errors}" var="error">
                            ${error}<br/>
                        </c:forEach>
                    </div>
                </c:if>
                </div>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th>Номер</th>
                            <th>Станции</th>
                            <th>Время в пути</th>
                            <th>Дистанция</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="route" items="${routes}">
                            <tr data-id="${route.getNumber()}">
                                <td>${route.getNumber()}</td>
                                <td>${route.getRoute()}</td>
                                <td>${route.getTime()}</td>
                                <td>${route.getDistance()}</td>
                                <td><a href="${pageContext.request.contextPath}/main/route/edit/${route.number}">редактировать</a></td>
                                <td><a class="js-delete-link" href="${pageContext.request.contextPath}/main/route/delete/${route.number}"
                                       data-id="${route.getNumber()}">удалить</a></td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td colspan="4"><a href="${pageContext.request.contextPath}/main/route/add">+ добавить маршрут</a></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>