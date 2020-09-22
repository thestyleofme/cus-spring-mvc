<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
</head>
<body>
<form action="/resume/list" method="post">
    <p><label for="username">username: </label><input type="text" id="username" name="username"/></p>
    <p><label for="password">password: </label><input type="text" id="password" name="password"/></p>
    <input type="submit" value="login">
</form>
</body>
</html>
