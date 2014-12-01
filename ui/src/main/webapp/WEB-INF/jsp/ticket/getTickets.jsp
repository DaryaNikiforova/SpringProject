<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 19.10.14
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="f" uri="http://example.com/functions" %>

<t:flatTemplate menuBlock="cabinet" menuRow="get" pageHeader="Билеты">
    <jsp:body>
        <div class="row">
            <div class="col-lg-6">
                <c:choose>
                    <c:when test="${tickets.isEmpty() || tickets == null}">На данный момент у вас нет купленных билетов</c:when>
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
                            <table class="table table-striped table-hover ">
                                <thead>
                                <tr>
                                    <th>Маршрут</th>
                                    <th>Поезд</th>
                                    <th>Дата отправления</th>
                                    <th>Дата прибытия</th>
                                    <th>Место</th>
                                    <th>Цена</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="ticket" items="${tickets}">
                                <tr data-id="${ticket.id}">
                                    <td><a href="${pageContext.request.contextPath}/main/cabinet/${ticket.id}">${ticket.route}</a></td>
                                    <td>${ticket.trip}</td>
                                    <td>${f:formatDate(ticket.departure)}</td>
                                    <td>${f:formatDate(ticket.arrival)}</td>
                                    <td>${ticket.seatNumber}</td>
                                    <td>${f:formatPrice(ticket.price)}</td>
                                </tr>
                                </c:forEach>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>