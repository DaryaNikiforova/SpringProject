
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:flatTemplate menuBlock="station" menuRow="edit" pageHeader="Редактирование станции">
    <jsp:body>
        <!--c:set var="nameCondition" value="{errors.station.name.empty}"-->

        <div class="row">
            <div class="col-lg-6">
                <div class="well">
                    <form:form class="form-horizontal" action="${pageContext.request.contextPath}/main/station/edit/${station.id}" method="post" modelAttribute="station">
                        <c:if test="${errors != null && !errors.isEmpty()}">
                            <div class="alert alert-danger">
                                <form:errors path="*"/>
                            </div>
                        </c:if>
                        <fieldset>
                            <!--<div class="form-group <--c:if test="{nameCondition}">has-error">-->
                            <label class="control-label col-lg-3">Имя станции:</label>
                            <div class="col-lg-9">
                                <form:input path="name" type="text" class="form-control" name="name" maxlength="100" value="${param.name}" required="required" />
                            </div>
                            <!--c:if test="{nameCondition}"><label class="text-danger col-lg-12">{errors}</label>-->
                            <!--/div-->
                            <div class="form-group">
                                <div class="col-lg-12">
                                    <button type="submit" class="btn btn-primary pull-right">Изменить</button>
                                </div>
                            </div>
                        </fieldset>
                    </form:form>
                </div>
            </div>
        </div>
    </jsp:body>
</t:flatTemplate>