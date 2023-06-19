<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Edit</title>
    <meta charset="UTF-8">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h3><a href="${pageContext.request.contextPath}/meals">Meals</a></h3>
<hr>
<h2>Meal</h2>
<form action="${pageContext.request.contextPath}/meals" method="post">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="datetime">DateTime
        <input type="datetime-local" name="datetime" id="datetime" value="${meal.dateTime}">
    </label><br>
    <label for="description">Description
        <input type="text" name="description" id="description" value="${meal.description}">
    </label><br>
    <label for="calories">Calories
        <input type="text" name="calories" id="calories" value="${meal.calories}">
    </label><br>
    <input type="submit" value="Save">
    <input type="button" onclick="window.history.back()" value="Cancel">
</form>
</body>
</html>