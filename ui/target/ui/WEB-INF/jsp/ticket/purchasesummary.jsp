<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 22.10.14
  Time: 2:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://example.com/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<t:flatTemplate pageHeader="Оформление билета">
    <jsp:body>
        <form:form role="form" class="form-horizontal" method="post" action="${pageContext.request.contextPath}/main/ticket/purchase" modelAttribute="ticket">
            <div class="row">
                <div class="col-md-7">
                    <c:if test="${isSuccess}">
                    <div class="alert alert-success">
                        Покупка успешно произведена!
                    </div>
                    </c:if>
                    <div class="panel panel-info">
                        <div class="panel-heading"><h3 class="panel-title">Рейс</h3></div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="control-label col-md-4">Маршрут поездки:</label>
                                <p class="form-control-static">${ticket.getRoute()}</p>
                                <label class="control-label col-md-4">Поезд:</label>
                                <p class="form-control-static">${ticket.getTrip()}</p>
                                <label class="control-label col-md-4">Отправление:</label>
                                <p class="form-control-static">${f:formatDate(ticket.getDeparture())}</p>
                                <label class="control-label col-md-4">Прибытие:</label>
                                <p class="form-control-static">${f:formatDate(ticket.getArrival())}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-7">
                    <div class="panel panel-info">
                        <div class="panel-heading"><h3 class="panel-title">Билет</h3></div>
                        <div class="panel-body">
                            <div class="form-group">
                                <label class="control-label col-md-4">Имя:</label>
                                <p class="form-control-static">${ticket.getUserName()}</p>
                                <label class="control-label col-md-4">Фамилия:</label>
                                <p class="form-control-static">${ticket.getUserSurname()}</p>
                                <label class="control-label col-md-4">Дата рождения:</label>
                                <p class="form-control-static">${ticket.getBirthDate()}</p>
                                <label class="control-label col-md-4">Место:</label>
                                <p class="form-control-static">${ticket.getSeatNumber()}</p>
                                <label class="control-label col-md-4">Тип:</label>
                                <p class="form-control-static">${ticket.getRateName()}</p>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-info">
                        <div class="panel-heading"><h3 class="panel-title">Доп. услуги</h3></div>
                        <div class="panel-body">
                            <div class="col-md-offset-3 col-md-10">
                                <c:forEach var="s" items="${ticket.getServices()}">
                                    <p class="form-control-static">${f:formatService(s.name, s.value)}</p>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-info">
                        <div class="panel-heading"><h3 class="panel-title">Цена</h3></div>
                        <div class="panel-body">
                            <p class="form-control-static">${f:formatPrice(ticket.getPrice())}</p>
                        </div>
                    </div>

                    <div class="form-group">
                        <c:if test="${!isSuccess}">
                        <h5><a href="${pageContext.request.contextPath}/buyTicket?tripId=${ticket.getTripId()}&stationFrom=${ticket.getStationFrom()}&stationTo=${ticket.getStationTo()}"
                               class="form-control-static col-md-6">&lt;&lt; Назад к оформлению</a></h5>
                        <div class="col-md-4 pull-right">
                            <button type="submit" class="btn btn-primary col-md-12">Купить</button>
                        </div>
                        </c:if>
                        <c:if test="${isSuccess}">
                            <h5><a href="${pageContext.request.contextPath}/main/search"
                            class="form-control-static col-md-6">&lt;&lt; Назад к поиску</a></h5>
                        </c:if>
                    </div>
                </div>
            </div>
        </form:form>
    </jsp:body>
</t:flatTemplate>