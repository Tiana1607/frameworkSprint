package listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext springContext = new ClassPathXmlApplicationContext(
                "applicationContext.xml"
        );
        sce.getServletContext().setAttribute("applicationContext", springContext);
        System.out.println("==== Spring démarré ====");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("==== Spring arrêté ====");
    }
}
