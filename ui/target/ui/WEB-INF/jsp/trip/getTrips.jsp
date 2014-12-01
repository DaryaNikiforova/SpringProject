<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 20.10.14
  Time: 0:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://example.com/functions" %>

<t:flatTemplate menuBlock="trip" menuRow="get" pageHeader="Рейсы">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/ajax-delete-items.js"></script>
        <script type="text/javascript">
            $(function () {
                ajaxHelper.setDeleteLinks('${pageContext.request.contextPath}/main/trip/delete');
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="row">
            <div class="col-md-8">
                <c:choose>
                <c:when test="${trips == null || trips.isEmpty()}">На данный момент никаких рейсов не задано</c:when>
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
                            <th>Маршрут</th>
                            <th>Отправление</th>
                            <th>Прибытие</th>
                            <th>Всего мест</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="trip" items="${trips}">
                            <tr data-id="${trip.getId()}">
                                <td><c:out value="${trip.getNumber()}"/></td>
                                <td>${trip.getRoute()}</td>
                                <td>${f:formatDate(trip.getDeparture())}</td>
                                <td>${f:formatDate(trip.getArrival())}</td>
                                <td><c:out value="${trip.getSeatCount()}"/></td>
                                <td class="text-right"><a href="${pageContext.request.contextPath}/main/trip/passengers?tripId=${trip.getId()}">посмотреть пассажиров</a></td>
                                <td><a class="js-delete-link" href="${pageContext.request.contextPath}/main/trip/delete/${trip.id}"
                                       data-id="${trip.getId()}">удалить</a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
                </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>