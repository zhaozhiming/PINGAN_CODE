$(document).ready(function () {
    dp.SyntaxHighlighter.ClipboardSwf = $("#syntaxHighLightFont").val();
    dp.SyntaxHighlighter.HighlightAll('code');

    $(".list .item").click(function () {
        window.find($.trim($(this).text()), false, false);
    });

});