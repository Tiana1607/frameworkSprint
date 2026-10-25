package mg.itu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mg.itu.annotation.auth.Authenticated;
import java.lang.reflect.Method;

public class AuthUtil {

    // Vrai si la méthode OU sa classe porte @Authenticated
    public static boolean isAuthRequired(Method method) {
        return method.isAnnotationPresent(Authenticated.class)
                || method.getDeclaringClass().isAnnotationPresent(Authenticated.class);
    }

    // Vrai si la session existe et contient la clé de connexion
    public static boolean isAuthenticated(HttpServletRequest req, String sessionKey) {
        HttpSession session = req.getSession(false); 
        return session != null && session.getAttribute(sessionKey) != null;
    }
}