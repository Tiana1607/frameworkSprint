package mg.itu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.reflect.Method;
import java.util.List;

public class JsonUtil {

    // Sérialisation JSON
    public static String toJson(Object obj) throws Exception {
        if (obj instanceof List<?>) {
            JSONArray array = new JSONArray();
            for (Object item : (List<?>) obj) {
                array.put(new JSONObject(item));
            }
            return array.toString();
        }
        return new JSONObject(obj).toString();
    }

    // Construction des arguments selon les types des paramètres
    public static Object[] buildArgs(Method method,
            HttpServletRequest req,
            HttpServletResponse resp,
            ServletContext context) {
        Class<?>[] paramTypes = method.getParameterTypes();
        Object[] args = new Object[paramTypes.length];

        for (int i = 0; i < paramTypes.length; i++) {
            String typeName = paramTypes[i].getName();
            if (typeName.equals("jakarta.servlet.http.HttpServletRequest")) {
                args[i] = req;
            } else if (typeName.equals("jakarta.servlet.http.HttpServletResponse")) {
                args[i] = resp;
            } else {
                String attrName = paramTypes[i].getSimpleName().substring(0, 1).toLowerCase()
                        + paramTypes[i].getSimpleName().substring(1);
                args[i] = context.getAttribute(attrName);
            }
        }
        return args;
    }
}
