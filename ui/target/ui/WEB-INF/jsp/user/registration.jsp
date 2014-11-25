<%--
  Created by IntelliJ IDEA.
  User: apple
  Date: 12.10.14
  Time: 1:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<t:mainTemplate page="registration">
    <jsp:attribute name="header">
    </jsp:attribute>
    <jsp:attribute name="footer">
        <script src="${pageContext.request.contextPath}/resources/js/moment-with-locales.min.js"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap-datetimepicker.min.js"></script>
        <script type="text/javascript">
            $(function () {
                var getMinDate = function() {
                    var today = new Date();
                    var minDate = new Date();
                    minDate.setYear(today.getYear() - 18);
                    return moment(minDate).format('DD.MM.YYYY');
                }
                $('.js-timepicker').datetimepicker({
                    pickTime: false,
                    language: 'ru',
                    minDate: '01.01.1900',
                    maxDate: getMinDate(),
                    showToday: false,
                    defaultDate: getMinDate()
                });
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <h2 class="text-center title simple">Регистрация</h2>
        <div class="row">
            <div class="col-sm-4 col-sm-offset-4">
                <form:form role="form" action="${pageContext.request.contextPath}/main/user/registration" method="post" modelAttribute="user">
                    <c:if test="${errors != null && !errors.isEmpty()}">
                        <div class="alert alert-danger">
                            <form:errors path="*"/>
                        </div>
                    </c:if>
                    <fieldset>
                        <div class="form-group">
                            <form:input path="login" required="required" class="form-control" placeholder="Логин" name="login" value="${param.login}" type="text" autofocus="true" maxlength="100" />
                        </div>
                        <div class="form-group">
                            <form:input path="password" required="required" class="form-control" placeholder="Пароль" name="password" type="password" value="" maxlength="100"/>
                        </div>
                        <div class="form-group">
                            <form:input path="name" required="required" class="form-control" placeholder="Имя" name="name" type="text" value="${param.name}" maxlength="100" />
                        </div>
                        <div class="form-group">
                            <form:input path="surname" required="required" class="form-control" placeholder="Фамилия" name="surname" type="text" value="${param.surname}" maxlength="100" />
                        </div>
                        <div class="form-group">
                            <div class='input-group date js-timepicker'>
                                <form:input path="birthDate" type='text' class="form-control" name="birth" placeholder="Дата рождения" required="required" value="${param.birth}" />
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </div>
                        <button class="btn btn-lg btn-default btn-block" type="submit">Зарегистрироваться</button>
                    </fieldset>
                </form:form>
            </div>
        </div>
    </jsp:body>
</t:mainTemplate>