<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:mainTemplate page="index" >
    <jsp:body>
        <div class="row">
            <div class="col-md-10 col-lg-10 col-md-offset-1 col-lg-offset-1 text-center">
                <h1 class="title">Trains'n'Tickets</h1>
                <h2 class="subtitle">Probably the best ticket service</h2>
                <!-- Short introductory (optional) -->
                <h3 class="tagline" style="margin-bottom: 10px;">
                    Воспользуйтесь удобным сервисом поиска и покупки ж/д билетов<br>
                    Начните отсюда<br>
                </h3>
                <h1 style="margin:0 0 20px 0; color: #ffffff">&darr;</h1>
                <p><a href="search" class="btn btn-default btn-lg">Перейти к поиску</a></p>
            </div>
        </div>
    </jsp:body>
</t:mainTemplate>