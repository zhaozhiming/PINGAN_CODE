$(document).ready(function () {

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
                showResult(data);

                $("tr").hover(function () {
                    $(this).toggleClass("positive");
                });
            });
        }
    };

    $('#searchForm').form(rules, setting);

    function showResult(data) {
        var result = jQuery.parseJSON(data);
        var resultContent = "";

        for (var i = 0; i < result.length; i++) {
            console.log(result[i]);
            resultContent += "<tr>";
            resultContent += "<td>" + result[i].jarName + "</td>";
            resultContent += "<td>" + result[i].version + "</td>";
            resultContent += "<td><a class='item' title='点击查看源码' target='_blank' href='";
            resultContent += $("#showUrl").val() + "?jarName=" + result[i].jarName + "&path=";
            resultContent += result[i].path + "'>" + result[i].path + "</a></td>";
            resultContent += "</tr>";
        }

        $('#result').html(resultContent);
    }
});


