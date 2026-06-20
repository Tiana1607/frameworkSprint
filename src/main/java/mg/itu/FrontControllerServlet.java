package mg.itu;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.util.ClassScanner;
import mg.itu.annotation.controller.Controller;
import java.io.*;
import java.util.List;

public class FrontControllerServlet extends HttpServlet {

    private List<Class<?>> controllers;

    @Override
    public void init() throws ServletException {
        super.init();

        String packageName = getServletConfig().getInitParameter("controllerPackage");

        // if (packageName == null || packageName.isEmpty()) {
        //     packageName = "controllers"; 
        // }

        try {
            controllers = ClassScanner.getClassesByAnnotation(packageName, Controller.class);
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
    }
}
