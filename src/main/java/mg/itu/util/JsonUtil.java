package mg.itu.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;
import org.json.JSONObject;
import org.json.JSONArray;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
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

    public static Object[] buildArgs(Method method, HttpServletRequest req) {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = params[i].getType();
            String parameterName = params[i].getName();
            String value = req.getParameter(parameterName);

            if (isSimpleType(type)) {
                args[i] = convert(value, type);
            } else {
                args[i] = buildObject(type, req, parameterName);
            }
        }

        return args;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type == String.class
                || type == int.class || type == Integer.class
                || type == double.class || type == Double.class
                || type == boolean.class || type == Boolean.class
                || type == long.class || type == Long.class;
    }

    private static Object convert(String value, Class<?> type) {
        if (type == String.class) {
            return value;
        }
        if (type == int.class || type == Integer.class) {
            return value != null ? Integer.parseInt(value) : 0;
        }
        if (type == double.class || type == Double.class) {
            return value != null ? Double.parseDouble(value) : 0.0;
        }
        if (type == boolean.class || type == Boolean.class) {
            return value != null && Boolean.parseBoolean(value);
        }
        if (type == long.class || type == Long.class) {
            return value != null ? Long.parseLong(value) : 0L;
        }

        return null;
    }

    private static Object buildObject(Class<?> type, HttpServletRequest req, String prefix) {
        try {
            Object object = type.getDeclaredConstructor().newInstance();

            for (PropertyDescriptor property
                    : Introspector.getBeanInfo(type, Object.class).getPropertyDescriptors()) {

                Method setter = property.getWriteMethod();
                if (setter != null) {
                    String value = req.getParameter(prefix + "." + property.getName());
                    if (value != null) {
                        Object converted = convert(value, property.getPropertyType());
                        setter.invoke(object, converted);
                    }
                }
            }
            return object;
        } catch (Exception e) {
            throw new IllegalArgumentException("Impossible de construire " + type.getName(), e);
        }
    }
}
