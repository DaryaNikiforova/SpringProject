<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 27.10.2014
  Time: 0:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:flatTemplate menuBlock="trip" pageHeader="Пассажиры рейса">
    <jsp:body>
        <div class="row">
            <div class="col-md-8">
                <c:choose>
                <c:when test="${passengers.isEmpty() || passengers == null}">На данный момент зарегистрированных пассажиров нет.</c:when>
                <c:otherwise>
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
			        <th>Имя</th>
			        <th>Фамилия</th>
			        <th>Дата рождения</th>
			        <th>Место</th>
			        <th>Маршрут пассажира</th>
                        </tr>
                        </thead>
                        <tbody>
			    <c:forEach var="p" items="${passengers}">
			        <tr>
			            <td>${p.getName()}</td>
			            <td>${p.getSurname()}</td>
			            <td>${p.getBirthDate()}</td>
			            <td>${p.getSeat()}</td>
			            <td>${p.getPassRoute()}</td>
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