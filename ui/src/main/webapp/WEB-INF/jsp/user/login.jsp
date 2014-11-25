<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 14.10.14
  Time: 22:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<c:url var="postLoginUrl" value="/j_spring_security_check" />

<t:mainTemplate page="login">
    <jsp:body>
        <c:if test="${param.failed == true}">
            <div>Your login attempt failed. Please try again.</div>
        </c:if>
        <h2 class="text-center title simple">Вход</h2>
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <form role="form" action="${postLoginUrl}" method="post">
                    <fieldset>
                        <div class="form-group">
                            <input required="required" class="form-control" placeholder="Логин" name="j_username" value="${param.login}" type="text" autofocus maxlength="100">
                        </div>
                        <div class="form-group">
                            <input required="required" class="form-control" placeholder="Пароль" name="j_password" type="password" value="" maxlength="100">
                        </div>
                        <div class="form-group text-center">
                            <p class="form-control-static">Еще не зарегистрировались? <a href="${pageContext.request.contextPath}/main/user/registration">Вам сюда!</a></p>
                        </div>
                        <button class="btn btn-lg btn-default btn-block" type="submit">Войти</button>
                    </fieldset>
                </form>
            </div>
        </div>
    </jsp:body>
</t:mainTemplate>
