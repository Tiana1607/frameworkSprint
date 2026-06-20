package mg.itu;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.ClassScanner;
import mg.itu.annotation.controller.Controller;
import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class FrontControllerServlet extends HttpServlet {

    private List<Class<?>> controllers;
    private Map<String, Map<Class<?>, List<Method>>> urlMapping;
    private List<String> urls;

    @Override
    public void init() throws ServletException {
        super.init();

        String packageName = getServletConfig().getInitParameter("controllerPackage");

        if (packageName == null || packageName.isEmpty()) {
            packageName = "controllers";
        }

        try {
            controllers = ClassScanner.getClassesByAnnotation(packageName, Controller.class);
            urlMapping = ClassScanner.getAnnotatedMethodsByUrl(Controller.class, packageName, UrlMapping.class);

            urls = ClassScanner.ifUrlExists(packageName);
        } catch (Exception e) {
            throw new ServletException("Erreur lors du scan des controllers", e);
        }
    }

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

        out.println("<h1>Controllers chargés au démarrage :</h1><ul>");
        for (Class<?> c : controllers) {
            out.println("<li>" + c.getName() + "</li>");
        }
        out.println("</ul>");

        out.println("<h2>Mappings URL :</h2>");
        out.println("<ul>");

        for (Map.Entry<String, Map<Class<?>, List<Method>>> urlEntry : urlMapping.entrySet()) {

            String url = urlEntry.getKey();

            if (!urls.contains(url)) {
                throw new ServletException(
                        "URL inconnue : " + url +
                                "\nURLs supportées : " + urls);
            }

            Map<Class<?>, List<Method>> classMap = urlEntry.getValue();

            for (Map.Entry<Class<?>, List<Method>> classEntry : classMap.entrySet()) {

                Class<?> controllerClass = classEntry.getKey();
                List<Method> methods = classEntry.getValue();

                for (Method method : methods) {

                    out.println("<li>"
                            + "\"" + url + "\" : "
                            + controllerClass.getSimpleName()
                            + " → "
                            + method.getName()
                            + "()"
                            + "</li>");
                }
            }
        }

        out.println("</ul>");
    }
}
