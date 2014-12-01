<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="f" uri="http://example.com/functions" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:flatTemplate pageHeader="Оформление билета">
    <jsp:body>
        <form:form role="form" class="form-horizontal" action="${pageContext.request.contextPath}/main/ticket/buy" modelAttribute="ticket">
        <div class="row">
            <div class="col-md-7">
                <c:if test="${error != null && !errors.isEmpty()}">
                    <div class="alert alert-danger">
                        <p>* ${error}</p>
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
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4">Место:</label>
                            <div class="col-md-6">
                                <form:select path="seatNumber" name="place" class="form-control" required="required">
                                    <option value="" disabled selected>Выберите место...</option>
                                    <c:forEach var="s" items="${ticket.getSeats()}">
                                        <form:option value="${s}">${s}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4">Тип:</label>
                            <div class="col-md-6">
                                <form:select path="rateType" name="rate" class="form-control" required="required">
                                    <option value="" disabled selected>Выберите тип...</option>
                                    <c:forEach var="s" items="${ticket.getRateTypes()}">
                                        <form:option value = "${s.key}">${s.value}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-info">
                    <div class="panel-heading"><h3 class="panel-title">Доп. услуги</h3></div>
                    <div class="panel-body">
                        <div class="col-md-offset-3 col-md-10">
                            <c:forEach items="${ticket.getServices()}" var="entry">
                                <div class="checkbox">
                                <form:checkbox path="services" value="${entry.getId()}" />
                                <c:out value="${f:formatService(entry.name, entry.value)}"/>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <h5><a href="${pageContext.request.contextPath}/main/search/" class="form-control-static col-md-6">&lt;&lt; Назад к рейсам</a></h5>
                    <div class="col-md-4 pull-right">
                        <button type="submit" class="btn btn-primary col-md-12">Оформить билет</button>
                    </div>
                </div>
            </div>
        </div>
       </form:form>
    </jsp:body>
</t:flatTemplate>


