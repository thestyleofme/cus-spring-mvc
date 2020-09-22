<%@ page isELIgnored="false" contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>简历列表</title>
</head>
<body>
<h2>简历列表</h2>
<div>
    <a href="/html/resume-add.html">添加</a>
    <table>
        <tr>
            <th>id</th>
            <th>name</th>
            <th>address</th>
            <th>phone</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${resumeList}" var="item">
            <tr>
                <td>${item.id}</td>
                <td>${item.name}</td>
                <td>${item.address}</td>
                <td>${item.phone}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/resume/pre-update?id=${item.id}">编辑</a>
                    <a href="${pageContext.request.contextPath}/resume/delete?id=${item.id}">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
