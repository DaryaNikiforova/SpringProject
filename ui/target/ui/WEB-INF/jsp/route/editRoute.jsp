<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 19.10.14
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<t:flatTemplate menuBlock="route" menuRow="edit" pageHeader="Редактирование маршрута">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/selectize.min.js"></script>
        <script type="text/javascript">
            $(function () {
                var stationOptions = [
                    <c:forEach var="station" items="${stations}">
                    {value: '${station.getName()}', text: '${station.getName()}'},
                    </c:forEach>
                ];

                var stationCount = <c:choose><c:when test="${route.routeEntries.size() > 2}">${route.routeEntries.size() - 1}</c:when><c:otherwise>1</c:otherwise></c:choose>;
                var lastPanel = $('.js-last-panel');
                var select = $('.js-station-select').selectize();
                select[0].selectize.setValue('${route.routeEntries[0].stationName}');
                select[1].selectize.setValue('${route.routeEntries[1].stationName}');

                var withIncreasedCount = function(func) {
                    func(++stationCount);
                }
                var withDeacreasedCount = function(func) {
                    func(--stationCount);
                }

                var getRemoveControl = function(count) {
                    return "<div class=\"form-group js-remove-control" + count + "\">" +
                            "<div class=\"col-md-2 pull-right\">" +
                            "<a class=\"js-remove-station" + count + "\" style=\"cursor: pointer;\">удалить</a>" +
                            "</div></div>";
                }

                var getStationControl = function(count) {
                    return "<div class=\"well js-panel" + count + "\">" +
                            "<div class=\"form-group\">" +
                            "<label class=\"control-label col-md-4\">Станция №" + (count + 1) + "</label>" +
                            "<div class=\"col-md-8\">" +
                            "<select class=\"js-station-select" + count + "\" placeholder=\"Выберете станцию...\" id=\"routeEntries"+ count + ".stationName\" name=\"routeEntries[" + count + "].stationName\" required>" +
                            "<option value=\"\">Выберете станцию...</option>" +
                            "</select>" +
                            "</div></div>" +
                            "<div class=\"form-group\">" +
                            "<label class=\"control-label col-md-4\">Время прибытия</label>" +
                            "<p class=\"form-control-static col-md-2 text-right\">часы</p>" +
                            "<div class=\"col-md-2\"><input type=\"number\" class=\"form-control\" min=\"0\" placeholder=\"0\" maxlength=\"4\" id=\"routeEntries"+ count + ".hour\" name=\"routeEntries[" + count + "].hour\" required></div>" +
                            "<p class=\"form-control-static col-md-2 text-right\">минуты</p>" +
                            "<div class=\"col-md-2\"><input type=\"number\" class=\"form-control\" min=\"0\" max=\"60\" placeholder=\"0\" id=\"routeEntries"+ count + ".minute\" name=\"routeEntries[" + count + "].minute\" required></div>" +
                            "</div>" +
                            "<div class=\"form-group\">" +
                            "<label class=\"control-label col-md-4\">Дистанция</label>" +
                            "<div class=\"col-md-2\"><input type=\"number\" min=\"0\" class=\"form-control\" placeholder=\"0\" maxlength=\"5\" id=\"routeEntries"+ count + ".distance\" name=\"routeEntries[" + count + "].distance\" required></div>" +
                            "</div>" + getRemoveControl(count) + "</div>";
                }

                var setRemoveStationAction = function(count) {
                    $('.js-remove-station' + count).click(function() {
                        $('.js-panel' + count).remove();
                        withDeacreasedCount(function(decreasedCount) {
                            $('.js-panel' + decreasedCount).append(getRemoveControl(decreasedCount));
                            setRemoveStationAction(decreasedCount);
                        });
                    });
                }

                if(stationCount > 1) {
                    $('.js-panel' + stationCount).append(getRemoveControl(stationCount));
                    setRemoveStationAction(stationCount);
                }

                $('.js-add-station').click(function() {
                    withIncreasedCount(function(count) {
                        $(lastPanel).before(getStationControl(count));
                        var select = $('.js-station-select' + count).selectize()[0].selectize;
                        for (var i = 0; i < stationOptions.length; i++) {
                            select.addOption(stationOptions[i]);
                        }
                        $('.js-remove-control' + (count-1)).remove();
                        setRemoveStationAction(count);
                    });
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <div class="row">
            <div class="col-md-8">
                <form:form role="form" class="form-horizontal" action="${pageContext.request.contextPath}/main/route/edit/${route.number}" method="post" modelAttribute="route">
                    <c:if test="${errors != null && !errors.isEmpty()}">
                        <div class="alert alert-danger">
                            <form:errors path="*"/>
                        </div>
                    </c:if>
                    <form:hidden path="number" />
                    <div class="well">
                        <div class="form-group">
                            <label class="control-label col-md-4">Станция №1</label>
                            <div class="col-md-8">
                                <form:select path="routeEntries[0].stationName" class="js-station-select" placeholder="Выберите станцию...">
                                    <form:option value="">Выберите станцию...</form:option>
                                    <c:forEach var="station" items="${stations}">
                                        <form:option value="${station.getName()}">${station.getName()}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                    </div>

                    <div class="well">
                        <div class="form-group">
                            <label class="control-label col-md-4">Станция №2</label>
                            <div class="col-md-8">
                                <form:select path="routeEntries[1].stationName" class=" js-station-select" placeholder="Выберите станцию...">
                                    <form:option value="">Выберите станцию...</form:option>
                                    <c:forEach var="station" items="${stations}">
                                        <form:option value="${station.getName()}">${station.getName()}</form:option>
                                    </c:forEach>
                                </form:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4">Время прибытия</label>
                            <p class="form-control-static col-md-2 text-right">часы</p>
                            <div class="col-md-2"><form:input path="routeEntries[1].hour" type="number" class="form-control" min="0" placeholder="0" maxlength="4" /></div>
                            <p class="form-control-static col-md-2 text-right">минуты</p>
                            <div class="col-md-2"><form:input path="routeEntries[1].minute" type="number" class="form-control" min="0" max="59" placeholder="0" /></div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-4">Дистанция</label>
                            <div class="col-md-2"><form:input path="routeEntries[1].distance" type="number" min="0" class="form-control" placeholder="0" maxlength="5" /></div>
                        </div>
                    </div>

                    <c:forEach items="${route.routeEntries}" varStatus="s" begin="2">
                        <div class="well js-panel${s.index}">
                            <div class="form-group">
                                <label class="control-label col-md-4">Станция №${s.index + 1}</label>
                                <div class="col-md-8">
                                    <form:select path="routeEntries[${s.index}].stationName" class=" js-station-select" placeholder="Выберите станцию...">
                                        <form:option value="">Выберите станцию...</form:option>
                                        <c:forEach var="station" items="${stations}">
                                            <form:option value="${station.getName()}">${station.getName()}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4">Время прибытия</label>
                                <p class="form-control-static col-md-2 text-right">часы</p>
                                <div class="col-md-2"><form:input path="routeEntries[${s.index}].hour" type="number" class="form-control" min="0" placeholder="0" maxlength="4" /></div>
                                <p class="form-control-static col-md-2 text-right">минуты</p>
                                <div class="col-md-2"><form:input path="routeEntries[${s.index}].minute" type="number" class="form-control" min="0" max="59" placeholder="0" /></div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-md-4">Дистанция</label>
                                <div class="col-md-2"><form:input path="routeEntries[${s.index}].distance" type="number" min="0" class="form-control" placeholder="0" maxlength="5" /></div>
                            </div>
                        </div>
                    </c:forEach>

                    <div class="well js-last-panel">
                        <div class="text-center">
                            <a class="js-add-station" style="cursor: pointer;">+ добавить станцию</a>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-lg-12">
                            <button type="submit" class="btn btn-primary pull-right">Изменить</button>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>
</body>
</html>