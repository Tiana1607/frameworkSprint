<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Mouvement" %>
<html>
    <body>
        <h1>Liste des mouvements</h1>
        <table border="1">
            <tr>
                <th>Libellé</th>
                <th>Montant</th>
            </tr>
            <%
                List<Mouvement> mouvements = (List<Mouvement>) request.getAttribute("mouvements");
                for (Mouvement m : mouvements) {
                %>
                <tr>
                    <td><%= m.getLibelle() %></td>
                    <td><%= m.getMontant() %></td>
                </tr>
                <% } %>
            </table>
        </body>
    </html>