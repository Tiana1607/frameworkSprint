<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Mouvement" %>
<html>
    <body>
        <%-- <h1>Liste des mouvements</h1>
        <table border="1">
            <tr>
                <th>Libellé</th>
                <th>Montant</th>
            </tr>
            <%
                List<Mouvement> mouvements = (List<Mouvement>) request.getAttribute("mouvements");
                if (mouvements != null) {
                    for (Mouvement m : mouvements) {
                    %>
                    <tr>
                        <td><%= m.getLibelle() %></td>
                        <td><%= m.getMontant() %></td>
                    </tr>
                    <%
                    }
                }
            %>
        </table> --%>

        <form action="${pageContext.request.contextPath}/app/ajouter" method="post">
            <input type="text" name="libelle" placeholder="Libellé"/>
            <input type="number" step="0.01" name="montant" placeholder="Montant"/>
            <button type="submit">Ajouter</button>
        </form><br><br>


        <% if (request.getAttribute("message") != null) { %>
        <p style="color:green"><%= request.getAttribute("message") %></p>
        <% } %>
    </body>
</html>