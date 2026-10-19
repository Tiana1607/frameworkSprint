package listener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ApplicationContext springContext = new ClassPathXmlApplicationContext(
                    "applicationContext.xml");
            sce.getServletContext().setAttribute("applicationContext", springContext);
            System.out.println("==== Spring démarré ====");
        } catch (RuntimeException e) {
            System.err.println("==== ERREUR Spring : " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Object context = sce.getServletContext().getAttribute("applicationContext");
        if (context instanceof ConfigurableApplicationContext configurableContext) {
            configurableContext.close();
        }
        System.out.println("==== Spring arrêté ====");
    }
}