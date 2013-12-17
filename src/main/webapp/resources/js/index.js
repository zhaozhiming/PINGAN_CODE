$(document).ready(function () {
    $("#searchBtn").click(function () {
        var searchUrl = $("#searchUrl").val();
        $.post(searchUrl, function (data) {
            $("#result").show();
        });
    });

});