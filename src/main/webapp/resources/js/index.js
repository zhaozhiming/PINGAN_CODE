$(document).ready(function () {

    $('#sidebarBtn').click(function () {
        $('#sidebar').sidebar('toggle');
    });

    var rules = {
        searchKeyword: {
            identifier: 'searchKeyword',
            rules: [
                {
                    type: 'empty',
                    prompt: '请输入查询关键字'
                }
            ]
        }
    };

    var setting = {
        onSuccess: function () {
            var searchUrl = $("#searchUrl").val();
            var data = {
                searchKeyword: $("#searchKeyword").val()
            };

            $.post(searchUrl, data, function (data) {
                test(data);
            });
        }
    };

    $('#searchForm').form(rules, setting);

    function test(data) {
        var result = jQuery.parseJSON(data);

        if (result.length === 0) {
            $("#result").html("无符合条件的源文件，请重新查询");
            return;
        }

        var resultContent = "<div class='ui animated celled large list'>";

        for (var i = 0; i < result.length; i++) {
            console.log(result[i]);
            resultContent += "<div class='item'>";
            resultContent += "<img class='ui avatar image' src='" + $("#sourceCodeImageUrl").val() + "'>";
            resultContent += "<div class='content'>";
            resultContent += "<a class='header' title='点击查看源码'>" + result[i].path + "</a>";
            resultContent += "<div class='description'>";
            resultContent += result[i].jarName + " v" + result[i].version;
            resultContent += "</div>";
            resultContent += "</div>";
            resultContent += "</div>";
        }
        resultContent += "</div>";

        $('#result').html(resultContent);

    }
});


