package mg.itu;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mg.itu.annotation.url.UrlMapping;
import mg.itu.util.ClassScanner;
import mg.itu.util.JsonUtil;
import mg.itu.util.ModelView;
import mg.itu.util.UrlMethod;
import mg.itu.annotation.controller.Controller;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import mg.itu.annotation.webapi.WebAPI;

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

        Map<UrlMethod, Method> urlMethodMap
                = (Map<UrlMethod, Method>) getServletContext().getAttribute("urlMethodMap");

        String url = req.getRequestURI().substring(req.getContextPath().length());
        url = url.substring("/app".length());
        String httpMethod = req.getMethod();
        UrlMethod key = new UrlMethod(url, httpMethod);
        Method method = urlMethodMap.get(key);

        if (method == null) {
            resp.sendError(404, "Aucune route : " + key);
            return;
        }

        try {
            Object controllerInstance = method.getDeclaringClass()
                    .getDeclaredConstructor()
                    .newInstance();

            Object[] args = JsonUtil.buildArgs(method, req, resp, getServletContext());
            Object result = method.invoke(controllerInstance, args);

            if (method.isAnnotationPresent(WebAPI.class)) {
                resp.setContentType("application/json;charset=UTF-8");
                PrintWriter out = resp.getWriter();
                out.println(JsonUtil.toJson(result));
            } else if (result instanceof ModelView) {
                ModelView mv = (ModelView) result;
                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }
                req.getRequestDispatcher("/WEB-INF/views/" + mv.getView() + ".jsp")
                        .forward(req, resp);

            } else if (result instanceof String || !method.isAnnotationPresent(WebAPI.class)) {
                resp.setContentType("text/html;charset=UTF-8");
                PrintWriter out = resp.getWriter();
                out.println((String) result);
            }

        } catch (Exception e) {
            throw new ServletException("Erreur invocation", e);
        }
    }

}
