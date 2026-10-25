package mg.itu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mg.itu.annotation.auth.Authenticated;
import java.lang.reflect.Method;

import mg.itu.annotation.auth.Role;
import java.util.Collection;

public class AuthUtil {
    // Vrai si la session existe et contient la clé de connexion
    public static boolean isAuthenticated(HttpServletRequest req, String sessionKey) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute(sessionKey) != null;
    }

    // @Role implique l'authentification
    public static boolean isAuthRequired(Method method) {
        return method.isAnnotationPresent(Authenticated.class)
                || method.getDeclaringClass().isAnnotationPresent(Authenticated.class)
                || getRequiredRoles(method) != null;
    }

    // Rôles exigés (la méthode est prioritaire sur la classe), null si aucun
    public static String[] getRequiredRoles(Method method) {
        Role role = method.getAnnotation(Role.class);
        if (role == null)
            role = method.getDeclaringClass().getAnnotation(Role.class);
        return role == null ? null : role.value();
    }

    // Vrai si le rôle de la session correspond à l'un des rôles exigés
    public static boolean hasRole(HttpServletRequest req, String roleKey, String[] required) {
        HttpSession session = req.getSession(false);
        if (session == null)
            return false;

        Object current = session.getAttribute(roleKey);
        if (current == null)
            return false;

        for (String r : required) {
            if (current instanceof Collection<?> c) { // plusieurs rôles stockés
                if (c.contains(r))
                    return true;
            } else if (r.equals(current.toString())) { // un seul rôle
                return true;
            }
        }
        return false;
    }
}