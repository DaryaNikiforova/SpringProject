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

<t:flatTemplate menuBlock="station" menuRow="get" pageHeader="Станции">
    <jsp:body>
        <div class="row">
        <div class="col-lg-6">
                <c:choose>
                <c:when test="${stations.isEmpty() || stations == null}">На данный момент никаких станций не задано</c:when>
                <c:otherwise>
        <c:if test="${errors != null && !errors.isEmpty()}">
            <div class="alert alert-danger">
                <c:forEach items="${errors}" var="error">
                    ${error}<br/>
                </c:forEach>
            </div>
        </c:if>
        <div class="table-responsive">
            <table class="table table-striped table-hover ">
                <thead>
                <tr>
                    <th>Имя станции</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="station" items="${stations}">
                <tr>
                    <td>${station.getName()}</td>
                    <td><a href="${pageContext.request.contextPath}/main/station/edit/${station.id}">редактировать</a>
                        <a href="${pageContext.request.contextPath}/main/station/delete/${station.id}">удалить</a></td>
                </tr>
                </c:forEach>
                <tr>
                    <td colspan="2"><a href="${pageContext.request.contextPath}/main//station/add">+ добавить станцию</a></td>
                </tr>
            </table>
        </div>
                </c:otherwise>
                </c:choose>
        </div>
        </div>
    </jsp:body>
</t:flatTemplate>