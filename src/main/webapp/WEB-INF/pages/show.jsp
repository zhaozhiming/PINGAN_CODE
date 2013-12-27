<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <title>source code</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/semantic.min.css'/>">
    <link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/SyntaxHighlighter.css'/>">

    <script type="text/javascript" src="<c:url value='/resources/js/jquery-2.0.3.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/semantic.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/shCore.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/shBrushJava.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/show.js'/>"></script>

</head>
<body>
<input id="syntaxHighLightFont" type="hidden" value="<c:url value='/resources/fonts/clipboard.swf'/>">

<div id="sidebar" class="ui left sidebar vertical menu">
    <div class="header item">方法列表：</div>
    <c:forEach items="${methods}" var="method">
        <a class="item" data-findtext="${method.findText}">
            <c:if test="${method.modifier == 'public'}">
                <i class="green circle icon"></i>
            </c:if>
            <c:if test="${method.modifier == 'protected'}">
                <i class="blue circle icon"></i>
            </c:if>
            <c:if test="${method.modifier == 'private'}">
                <i class="red circle icon"></i>
            </c:if>
            <c:if test="${method.modifier == 'default'}">
                <i class="purple circle icon"></i>
            </c:if>
                ${method.showText}
        </a>
    </c:forEach>
</div>

<div class="ui divided grid">
    <div class="sixteen wide column">
        <pre name="code" class="java">
            ${sourceCode}
        </pre>
    </div>
</div>

</body>
</html>