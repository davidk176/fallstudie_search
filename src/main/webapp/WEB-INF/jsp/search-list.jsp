<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Add Book</title>
    </head>
    <body>
        <c:if test="${searchListSuccess}">
            <div>Found: ${result}</div>
        </c:if>

        <c:url var="search_list_url" value="/search/list"/>
        <form:form action="${search_list_url}" method="post" modelAttribute="searchInput">
            <form:label path="searchString">Search String: </form:label> <form:input type="text" path="searchString"/>
            <input type="submit" value="submit"/>
        </form:form>
    </body>
</html>