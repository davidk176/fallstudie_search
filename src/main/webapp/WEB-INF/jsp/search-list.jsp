<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Suche</title>
    </head>
    <body>
        <c:url var="search_list_url" value="/list/search"/>
        <form:form action="${search_list_url}" method="post" modelAttribute="searchInput">
            <form:label path="searchString">Suchbegriff: </form:label> <form:input type="text" path="searchString"/>
            <input type="submit" value="submit"/>
        </form:form>
        <c:if test="${result.isSuccessful()}">
            <div>Found ${result.getHits()} hits in ${result.getTook()} ms</div>
            <div>Result: ${result.getResult()}</div>
        </c:if>
    </body>
</html>