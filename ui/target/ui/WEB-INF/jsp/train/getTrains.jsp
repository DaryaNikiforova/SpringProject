<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 19.10.14
  Time: 16:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"  pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:flatTemplate menuBlock="train" menuRow="get" pageHeader="Поезда">
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/ajax-delete-items.js"></script>
        <script type="text/javascript">
            $(function () {
                ajaxHelper.setDeleteLinks('${pageContext.request.contextPath}/main/train/delete');
            });
        </script>
    </jsp:attribute>
    <jsp:body>
        <div class="row">
        <div class="col-md-8">
            <c:choose>
            <c:when test="${trains.isEmpty() || trains == null}">На данный момент никаких поездов не задано</c:when>
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
                        <th>Номер поезда</th>
                        <th>Имя</th>
                        <th>Количество мест</th>
                        <th>Тип поезда</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="train" items="${trains}">
                        <tr data-id="${train.getNumber()}">
                            <td>${train.getNumber()}</td>
                            <td>${train.getName()}</td>
                            <td>${train.getSeatCount()}</td>
                            <td>${train.getRateName()}</td>
                            <td><a href="${pageContext.request.contextPath}/main/train/edit/${train.number}">редактировать</a>
                                <a class="js-delete-link" href="${pageContext.request.contextPath}/main/train/delete/${train.number}" data-id="${train.number}">удалить</a></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td colspan="5"><a href="${pageContext.request.contextPath}/main/train/add">+ добавить поезд</a></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            </c:otherwise>
            </c:choose>
        </div>
    </jsp:body>
</t:flatTemplate>
