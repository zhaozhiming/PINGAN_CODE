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
                $("#result").show();
            });
        }
    };

    $('#searchForm').form(rules, setting);
});