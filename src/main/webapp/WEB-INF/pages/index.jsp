<!doctype html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/semantic.min.css'/>">

    <script type="text/javascript" src="<c:url value='/resources/js/jquery-2.0.3.min.js'/>"></script>
    <script type="text/javascipt" src="<c:url value='/resources/js/semantic.min.js'/>"></script>
</head>
<body>

<div class="ui form">
    <div class="two fields">
        <div class="field">
            <div class="ui icon input">
                <input type="text" placeholder="Search huge...">
                <i class="search icon"></i>
            </div>
        </div>

        <div class="field">
            <div class="ui blue submit button">查询</div>
        </div>
    </div>
</div>

</body>
</html>