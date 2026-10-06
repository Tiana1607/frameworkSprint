package mg.itu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import org.json.JSONObject;
import org.json.JSONArray;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    public static Object[] buildArgs(Method method, HttpServletRequest req) {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = params[i].getType();
            String name = params[i].getName(); 

            String value = req.getParameter(name);

            if (type == String.class) {
                args[i] = value;
            } else if (type == int.class || type == Integer.class) {
                args[i] = value != null ? Integer.parseInt(value) : 0;
            } else if (type == double.class || type == Double.class) {
                args[i] = value != null ? Double.parseDouble(value) : 0.0;
            } else if (type == boolean.class || type == Boolean.class) {
                args[i] = value != null ? Boolean.parseBoolean(value) : false;
            } else if (type == long.class || type == Long.class) {
                args[i] = value != null ? Long.parseLong(value) : 0L;
            } else {
                args[i] = null;
            }
        }
        return args;
    }
}
