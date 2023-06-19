<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="ru">
<head>
    <title>Meals</title>
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
        <th></th>
        <th></th>
    </tr>
    <c:forEach var="meal" items="${requestScope.meals}" >
        <tr
        <c:if test="${meal.excess == true}">
            style="color: red"
        </c:if>
        <c:if test="${meal.excess == false}">
            style="color: green"
        </c:if>
        >
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" />
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td> <a href="${pageContext.request.contextPath}/meals?action=edit&mealId=${meal.id}">UPDATE</a></td>
            <td> <a href="${pageContext.request.contextPath}/meals?action=delete&mealId=${meal.id}">DELETE</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="${pageContext.request.contextPath}/meals?action=insert">Add Meal</a></p>
</body>
</html>