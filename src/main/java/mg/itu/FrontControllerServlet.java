package mg.itu;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.util.ClassScanner;
import mg.itu.annotation.controller.Controller;
import java.io.*;
import java.util.List;

public class FrontControllerServlet extends HttpServlet {

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

        try {
            List<Class<?>> controllers = ClassScanner.getClassesByAnnotation(
                    "controllers", Controller.class
            );

            out.println("<h1>Controllers trouvés :</h1><ul>");
            for (Class<?> c : controllers) {
                out.println("<li>" + c.getName() + "</li>");
            }
            out.println("</ul>");

        } catch (Exception e) {
            out.println("<h1>Erreur scan :</h1><pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
    }
}
