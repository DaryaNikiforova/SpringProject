<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:message var="pageHeader" code="searchByStation.pageHeader"/>
<spring:message var="stationPlaceholder" code="searchByStation.placeholder.station"/>
<spring:message var="datePlaceholder" code="searchByStation.placeholder.date"/>

<t:flatTemplate pageHeader="${pageHeader}">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/moment-with-locales.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datetimepicker.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/selectize.min.js"></script>
        <script type="text/javascript">
            $(function () {
                var getMaxDate = function() {
                    var today = new Date();
                    var maxDate = new Date();
                    maxDate.setDate(today.getDate() + 45);
                    return moment(maxDate).format('DD.MM.YYYY');
                }
                $('.js-station-select').selectize()[0].selectize.setValue('${param.station}');
                $('.js-timepicker').datetimepicker({
                    pickTime: false,
                    language: 'ru',
                    minDate: moment(new Date()).format('DD.MM.YYYY'),
                    maxDate: getMaxDate()
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <div class="col-lg-12">
                <ul class="nav nav-tabs">
                    <li><a href="${pageContext.request.contextPath}/main/search">
                        <spring:message code="search.label.searchTab"/></a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/main/search/byStation">
                        <spring:message code="search.label.searchByStationTab"/></a></li>
                </ul>
            </div>
            <div class="col-md-8">
                <div class="well">
                    <form:form role="form" action="./byStation" method="post" class="form-horizontal" modelAttribute="searchStation">
                        <c:if test="${errors != null && !errors.isEmpty()}">
                            <div class="alert alert-danger">
                                <form:errors path="*"/>
                            </div>
                        </c:if>
                        <div class="form-group">
                            <label class="control-label col-md-2"><spring:message code="searchByStation.label.station"/></label>
                            <div class="col-md-4">
                                <form:select path="station" class="js-station-select" placeholder="${stationPlaceholder}"
                                             name="station" required="required">
                                    <form:option value=""><spring:message code="searchByStation.placeholder.station"/></form:option>
                                    <c:forEach var="station" items="${stations}">
                                        <form:option value="${station.getName()}">${station.getName()}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                            <label class="control-label col-md-1"><spring:message code="searchByStation.label.date"/></label>
                            <div class="col-md-5">
                                <div class='input-group date js-timepicker'>
                                    <form:input path="date" type='text' class="form-control" placeholder="${datePlaceholder}"
                                                name="date" required="required" value="${param.date}"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-lg-12">
                                <button type="submit" class="btn btn-primary pull-right">
                                    <spring:message code="searchByStation.button.submit"/>
                                </button>
                            </div>
                        </div>
                    </form:form>
                </div>
            </div>
        </div>
        <!-- /.row -->
        <c:if test="${isPost && (stationTimetable == null || stationTimetable.isEmpty())}">
            <spring:message code="searchByStation.label.noResult"/>
        </c:if>
        <c:if test="${stationTimetable != null && !stationTimetable.isEmpty()}">
        <div class="row">
            <div class="col-md-8">
                <div class="table-responsive">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th><spring:message code="searchByStation.th.number"/></th>
                            <th><spring:message code="searchByStation.th.route"/></th>
                            <th><spring:message code="searchByStation.th.departureTime"/></th>
                            <th><spring:message code="searchByStation.th.arrivalTime"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="entry" items="${stationTimetable}">
                            <tr>
                                <td>${entry.getTrainNumber()}</td>
                                <td>${entry.getRouteName()}</br>${entry.getTrainName()}</td>
                                <td>${entry.getDepDate()}</br>${entry.getStationFrom()}</td>
                                <td>${entry.getArriveDate()}</br>${entry.getStationTo()}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </c:if>
    </jsp:body>
</t:flatTemplate>