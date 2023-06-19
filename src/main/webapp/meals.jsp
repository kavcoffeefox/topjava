<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<table border="1">
    <caption>Приемы пищи</caption>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${requestScope.meals}" >
        <tr
        <c:if test="${meal.excess == true}">
            style="color: red"
        </c:if>
        >
            <td>
                <fmt:parseDate value="${meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" />
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>