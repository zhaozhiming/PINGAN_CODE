$(document).ready(function () {
    dp.SyntaxHighlighter.ClipboardSwf = $("#syntaxHighLightFont").val();
    dp.SyntaxHighlighter.HighlightAll('code');

    $(".sidebar a").click(function () {
        window.find($.trim($(this).data("findtext")), false, false);
    });

    $('#sidebar').sidebar('toggle');

    $('.circle.icon').popup();

});