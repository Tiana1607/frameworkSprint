<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <body>
        <h2><%= request.getAttribute("message") %></h2>

        <form action="${pageContext.request.contextPath}/app/login" method="post">
            <input type="text" name="nom" placeholder="Nom"/>
            <button type="submit">Se connecter</button>
        </form>

        <a href="${pageContext.request.contextPath}/app/profil">Voir mon profil</a> |
        <a href="${pageContext.request.contextPath}/app/logout">Se déconnecter</a>
    </body>
</html>