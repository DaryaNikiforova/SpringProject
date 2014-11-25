<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 19.10.14
  Time: 17:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:flatTemplate menuBlock="train" menuRow="add" pageHeader="Новый поезд">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/selectize.min.js"></script>
        <script type="text/javascript">
            var select = $('.js-select').selectize();
            select[0].selectize.setValue('${train.rateId}');
        </script>
    </jsp:attribute>
    <jsp:body>
        <!--c:set var="numCondition" value="{errors.get('number').length() > 0}"/-->
        <!--c:set var="nameCondition" value="{errors.get('name').length() > 0}"/-->
        <!--c:set var="seatCondition" value="{errors.get('seatCount').length() > 0}"/-->
        <!--c:set var="rateCondition" value="{errors.get('rate').length() > 0}"/-->

        <div class="row">
            <div class="col-lg-6">
                <div class="well">
                    <form:form class="form-horizontal" action="./add" method="post" modelAttribute="train">
                        <c:if test="${errors != null && !errors.isEmpty()}">
                            <div class="alert alert-danger">
                                <form:errors path="*"/>
                            </div>
                        </c:if>
                        <fieldset>
                            <div class="form-group <c:if test="${numCondition}">has-error</c:if>">
                                <label class="control-label col-lg-3">Номер</label>
                                <div class="col-lg-9"><form:input path="number" type="number" class="form-control" min="1" maxlength="7" placeholder="1099" name="number" value="${param.number}" required="required" /></div>
                                <!--c:if test="{numCondition}"><label class="text-danger col-lg-12">{errors.get("number")}</label><--/c:if-->
                            </div>
                            <div class="form-group <c:if test="${nameCondition}">has-error</c:if>">
                                <label class="control-label col-lg-3">Имя</label>
                                <div class="col-lg-9"><form:input path="name" type="text" class="form-control" maxlength="100" placeholder="Сапсан (не обязательно)" name="name" value="${param.name}" /></div>
                                <!--c:if test="{nameCondition}"><label class="text-danger col-lg-12">{errors.get("name")}</label><-/c:if-->
                            </div>
                            <div class="form-group <c:if test="${seatCondition}">has-error</c:if>">
                                <label class="control-label col-lg-3">Количество мест</label>
                                <div class="col-lg-9"><form:input path="seatCount" type="number" class="form-control" min="1" placeholder="100" maxlength="4" name="seatCount" value="${param.seatCount}" required="required" /></div>
                                <!--c:if test="{seatCondition}"><label class="text-danger col-lg-12">{errors.get("seatCount")}</label><--/c:if-->
                            </div>
                            <div class="form-group <c:if test="${rateCondition}">has-error</c:if>">
                                <label class="control-label col-lg-3">Тип поезда</label>
                                <div class="col-lg-9">
                                    <form:select path="rateId" class="form-control js-select" placeholder="Выберите тип..." name="rate" required="required">
                                        <option value="" disabled selected>Выберите тип...</option>
                                        <c:forEach var="rate" items="${rates}">
                                            <form:option value="${rate.getId()}">${rate.getName()}</form:option>
                                        </c:forEach>
                                    </form:select>
                                </div>
                                <!--c:if test="{rateCondition}"><label class="text-danger col-lg-12">{errors.get("rate")}</label><--/c:if-->
                            </div>
                            <div class="form-group">
                                <div class="col-lg-12">
                                    <button type="submit" class="btn btn-primary pull-right">Создать</button>
                                </div>
                            </div>
                        </fieldset>
                    </form:form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>