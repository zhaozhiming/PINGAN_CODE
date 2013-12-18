<html>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html; charset=UTF-8" %>
<head>
    <title>source code</title>
    <link type="text/css" rel="stylesheet" href="<c:url value='/resources/css/SyntaxHighlighter.css'/>">

    <script type="text/javascript" src="<c:url value='/resources/js/jquery-2.0.3.min.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/shCore.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/shBrushJava.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/show.js'/>"></script>

</head>
<body>
<input id="syntaxHighLightFont" type="hidden" value="<c:url value='/resources/fonts/clipboard.swf'/>">

<pre name="code" class="java">
${sourceCode}
</pre>

</body>
</html>