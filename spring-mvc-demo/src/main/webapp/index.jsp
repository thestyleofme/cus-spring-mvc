<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>SpringMVC 测试页</title>

    <script type="text/javascript" src="/js/jquery.min.js"></script>

    <script>
        $(function () {
            $("#ajaxBtn").bind("click",function () {
                // 发送ajax请求
                $.ajax({
                    url: '/demo/handle07',
                    type: 'POST',
                    data: '{"id":1,"name":"李四"}',
                    contentType: 'application/json;charset=utf-8',
                    dataType: 'json',
                    success: function (data) {
                        alert(data.name);
                    }
                })
            })
        })
    </script>

    <style>
        div {
            padding: 10px 10px 0 10px;
        }
    </style>
</head>
<body>
<div>
    <h2>Spring MVC 请求参数绑定</h2>
    <fieldset>
        <legend>测试用例：SpringMVC 对原生servlet api的支持</legend>
        <a href="/demo/handle02?id=1">点击测试</a>
    </fieldset>
    <fieldset>
        <legend>测试用例：SpringMVC 接收简单数据类型参数</legend>
        <p>使用@RequestParam 接收简单数据类型参数(形参名和参数名不一致)</p>
        <a href="/demo/handle03?id=1">点击测试</a>
    </fieldset>
    <fieldset>
        <legend>测试用例：SpringMVC接收pojo类型参数</legend>
        <a href="/demo/handle04?id=1&name=zhangsan">点击测试</a>
    </fieldset>

    <fieldset>
        <legend>测试用例：SpringMVC接收pojo包装类型参数</legend>
        <a href="/demo/handle05?user.id=1&user.name=zhangsan">点击测试</a>
    </fieldset>

    <fieldset>
        <legend>测试用例：SpringMVC接收日期类型参数</legend>
        <a href="/demo/handle06?birthday=2020-09-12">点击测试</a>
    </fieldset>
</div>

<div>
    <h2>SpringMVC对Restful风格url的支持</h2>
    <fieldset>
        <legend>测试用例：SpringMVC对Restful风格url的支持</legend>

        <a href="/demo/handle/15">rest get测试</a>

        <form method="post" action="/demo/handle">
            <label>
                username: <input type="text" name="username"/>
            </label>
            <input type="submit" value="提交rest post请求"/>
        </form>

        <form method="post" action="/demo/handle/15/lisi">
            <input type="hidden" name="_method" value="put"/>
            <input type="submit" value="提交rest put请求"/>
        </form>

        <form method="post" action="/demo/handle/15">
            <input type="hidden" name="_method" value="delete"/>
            <input type="submit" value="提交rest delete请求"/>
        </form>
    </fieldset>
</div>

<div>
    <h2>Ajax json交互</h2>
    <fieldset>
        <legend>Ajax json交互</legend>
        <input type="button" id="ajaxBtn" value="ajax提交"/>
    </fieldset>
</div>

<div>
    <h2>multipart 文件上传</h2>
    <fieldset>
        <legend>multipart 文件上传</legend>
        <%--
            1 method="post"
            2 enctype="multipart/form-data"
            3 type="file"
        --%>
        <form method="post" enctype="multipart/form-data" action="/demo/upload">
            <input type="file" name="uploadFile"/>
            <input type="submit" value="上传"/>
        </form>
    </fieldset>
</div>
</body>
</html>
