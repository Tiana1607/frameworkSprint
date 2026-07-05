package mg.itu;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.ClassScanner;
import mg.itu.util.UrlMethod;
import mg.itu.annotation.controller.Controller;
import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {

    // private Map<UrlMethod, Method> urlMethodMap;

    // @Override
    // public void init() throws ServletException {
    //     super.init();
    //     String packageName = getServletConfig().getInitParameter("controllerPackage");
    //     if (packageName == null || packageName.isEmpty()) {
    //         packageName = "controllers";
    //     }

    //     try {
    //         urlMethodMap = ClassScanner.getUrlMethodMap(packageName);
    //     } catch (Exception e) {
    //         throw new ServletException("Erreur init scan", e);
    //     }
    // }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Map<UrlMethod, Method> urlMethodMap = (Map<UrlMethod, Method>) getServletContext().getAttribute("urlMethodMap");

        // Affiche toutes les routes enregistrées
        out.println("<h2>Routes enregistrées :</h2><ul>");
        for (Map.Entry<UrlMethod, Method> entry : urlMethodMap.entrySet()) {
            out.println("<li>" + entry.getKey() + " → " + entry.getValue().getName() + "</li>");
        }
        out.println("</ul>");

        // Affiche la route demandée
        String url = req.getRequestURI().substring(req.getContextPath().length());
        url = url.substring("/app".length());
        String httpMethod = req.getMethod();
        UrlMethod key = new UrlMethod(url, httpMethod);
        Method method = urlMethodMap.get(key);

        out.println("<h2>Requête actuelle : " + key + "</h2>");
        if (method != null) {
            out.println("<p style='color:green'> Route trouvée → " + method.getName() + "</p>");
        } else {
            out.println("<p style='color:red'> Aucune route trouvée</p>");
        }

        if (method == null) {
            resp.sendError(404, "Aucune route : " + key);
            return;
        }

        try {
            Object controllerInstance = method.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();

            String result = (String) method.invoke(controllerInstance);

            // Afficher le texte
            out.println(result);

        } catch (Exception e) {
            throw new ServletException("Erreur invocation", e);
        }
    }
}
