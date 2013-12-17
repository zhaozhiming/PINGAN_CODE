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

<div class="ui transparent inverted main menu">
    <div class="container">
        <a class="launch item" href="<c:url value='/'/>"><i class="icon list layout"></i> 首页</a>

        <div class="title item">
            <b>PAFA CODE</b>
        </div>
    </div>
</div>

<div class="ui grid">
    <div class="one wide column"></div>

    <div class="fifteen wide column">
        <div id="searchForm" class="ui form">
            <div class="two fields">
                <div class="field">
                    <div class="ui icon input">
                        <input id="searchKeyword" type="text" name="searchKeyword" placeholder="请输入查询关键字...">
                        <i class="search icon"></i>
                    </div>
                </div>

                <div class="field">
                    <div id="searchBtn" class="ui blue submit button">查询</div>
                </div>
            </div>
            <div class="ui error message"></div>
        </div>

        <div class="ui divider"></div>

        <div id="result" style="display: none">
            <table class="ui table segment">
                <thead>
                <tr>
                    <th>Jar包名</th>
                    <th>版本</th>
                    <th>路径</th>
                </tr>
                </thead>
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
    </div>
</div>

<div class="ui divider"></div>

<div class="ui inverted segment center aligned">
    <div class="ui inverted horizontal relaxed divided tiny list">
        <div class="item">Super-powered by 六脉神贱 ©2013-2015</div>
    </div>
</div>

</body>
</html>