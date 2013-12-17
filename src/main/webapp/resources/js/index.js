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
        onSuccess : function () {
            var searchUrl = $("#searchUrl").val();
            $.post(searchUrl, function (data) {
                $("#result").show();
            });
        }
    }

    $('#searchForm').form(rules, setting);
});