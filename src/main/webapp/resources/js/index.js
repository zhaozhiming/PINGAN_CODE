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
                updateList(data);
            });
        }
    };
    $('#searchForm').form(rules, setting);

    function updateList(content) {

        $("#result").show();

        var list = eval(content);
        $('#resultList').html('');

        for (var i = 0; i < list.length; i++) {
            var $tr = $('<tr>');

            $tr.attr('jarName', list[i].jarName);
            $tr.attr('version', list[i].version);
            $tr.attr('path', list[i].path);

            $tr.append($('<td>').text(list[i].jarName));
            $tr.append($('<td>').text(list[i].version));
            $tr.append($('<td>').text(list[i].path));

            $tr.click(function () {
                alert($(this).attr('jarName'));
            });

            $tr.hover(function () {
                $(this).toggleClass("negative");
            });

            $('#resultList').append($tr);
        }
    }


});


