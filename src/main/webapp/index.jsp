<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Weather App</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/weather" method="get">
    <label for="country">Страна:</label>
    <input type="text" name="country" id="country">
    <label for="city">Город:</label>
    <input type="text" name="city" id="city">
    <input type="submit" value="Get Weather">
</form>
</body>
</html>