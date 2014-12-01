<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 26.11.14
  Time: 20:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:flatTemplate pageHeader="500 Internal Server Error">
    <div class="row">
        <div class="col-md-12">
            <div class="error-template">
                <h1>Ошибка сервера</h1>
                <div class="error-details">
                </div>
                <div class="error-actions">
                    <a href="${pageContext.request.contextPath}/main/index" class="btn btn-primary btn-lg">
                        <span class="glyphicon glyphicon-home"></span>На главную</a>
                </div>
            </div>
        </div>
    </div>
</t:flatTemplate>
