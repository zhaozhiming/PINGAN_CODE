<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/semantic.min.css'/>">

    <script type="text/javascript" src="<c:url value='/resources/js/jquery-2.0.3.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/semantic.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/index.js'/>"></script>
</head>
<body>

<input id="searchUrl" type="hidden" value="<c:url value='/search'/>">

<div class="ui form">
    <div class="two fields">
        <div class="field">
            <div class="ui icon input">
                <input type="text" placeholder="Search huge...">
                <i class="search icon"></i>
            </div>
        </div>

        <div class="field">
            <div id="searchBtn" class="ui blue submit button">查询</div>
        </div>
    </div>
</div>

<div id="result" style="display: none">
    <table class="ui table segment">
        <thead>
        <tr><th>Jar包名</th>
            <th>版本</th>
            <th>路径</th>
        </tr></thead>
        <tbody>
        <tr>
            <td>Spring</td>
            <td>3.0</td>
            <td>org/springframework/stereotype/Controller</td>
        </tr>
        <tr>
            <td>Spring</td>
            <td>3.0</td>
            <td>org/springframework/stereotype/Controller</td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>