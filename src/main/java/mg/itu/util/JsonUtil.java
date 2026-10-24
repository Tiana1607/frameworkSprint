package mg.itu.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import org.json.JSONArray;
import org.json.JSONObject;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import jakarta.servlet.ServletContext;

public class JsonUtil {

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

    public static Object[] buildArgs(Method method, HttpServletRequest req, ServletContext servletContext)
            throws Exception {
        Parameter[] params = method.getParameters();
        Object[] args = new Object[params.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = params[i].getType();
            String name = params[i].getName();

            if (type == MySession.class) {
                args[i] = new MySession(req.getSession(true)); // crée la session si elle n'existe pas

            }
            else if (type == MapFile.class) {
                args[i] = buildMapFile(req, name);

            } else if (type == MapFile[].class) {
                args[i] = buildMapFiles(req, name);

            } else if (List.class.isAssignableFrom(type)
                    && params[i].getParameterizedType() instanceof java.lang.reflect.ParameterizedType pt
                    && pt.getActualTypeArguments()[0] == MapFile.class) {
                MapFile[] mapFiles = buildMapFiles(req, name);
                args[i] = new ArrayList<MapFile>(Arrays.asList(mapFiles));
            } else if (isSimpleType(type)) {
                args[i] = convert(req.getParameter(name), type);

            } else if (servletContext != null && servletContext.getAttribute(
                    type.getSimpleName().substring(0, 1).toLowerCase()
                            + type.getSimpleName().substring(1)) != null) {
                String attrName = type.getSimpleName().substring(0, 1).toLowerCase()
                        + type.getSimpleName().substring(1);
                args[i] = servletContext.getAttribute(attrName);

            } else {
                String attrName = type.getSimpleName().substring(0, 1).toLowerCase()
                        + type.getSimpleName().substring(1);
                Object attr = servletContext.getAttribute(attrName);
                System.out.println("DEBUG attrName=" + attrName + " attr=" + attr);
                if (servletContext != null && attr != null) {
                    args[i] = attr;
                } else {
                    args[i] = buildObject(type, req, name);
                }
            }
        }
        return args;
    }

    private static boolean isSimpleType(Class<?> type) {
        return type == String.class
                || type == int.class || type == Integer.class
                || type == double.class || type == Double.class
                || type == boolean.class || type == Boolean.class
                || type == long.class || type == Long.class
                || type == float.class || type == Float.class;
    }

    private static Object convert(String value, Class<?> type) {
        if (value == null) {
            if (type.isPrimitive())
                return 0;
            return null;
        }
        if (type == String.class)
            return value;
        if (type == int.class || type == Integer.class)
            return Integer.parseInt(value);
        if (type == double.class || type == Double.class)
            return Double.parseDouble(value);
        if (type == float.class || type == Float.class)
            return Float.parseFloat(value);
        if (type == long.class || type == Long.class)
            return Long.parseLong(value);
        if (type == boolean.class || type == Boolean.class)
            return Boolean.parseBoolean(value);
        return null;
    }

    private static Object buildObject(Class<?> type, HttpServletRequest req, String prefix)
            throws Exception {
        Object object = type.getDeclaredConstructor().newInstance();

        for (PropertyDescriptor property : Introspector.getBeanInfo(type, Object.class).getPropertyDescriptors()) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                String value = req.getParameter(prefix + "." + property.getName());
                if (value != null) {
                    setter.invoke(object, convert(value, property.getPropertyType()));
                }
            }
        }
        return object;
    }

    public static MapFile buildMapFile(HttpServletRequest req, String name)
            throws IOException, ServletException {

        if (!isMultipart(req))
            return null;

        Part part = req.getPart(name);
        if (part == null)
            return null;

        String submittedFilename = part.getSubmittedFileName();

        if (submittedFilename == null) {
            throw new IllegalArgumentException(
                    "Le champ " + name + " doit être un fichier");
        }

        if (submittedFilename.isEmpty())
            return null;

        String filename = extractFilename(submittedFilename);
        String extension = extractExtension(filename);
        byte[] bytes = readBytes(part);

        return new MapFile(filename, extension, bytes);
    }

    private static boolean isMultipart(HttpServletRequest req) {
        String contentType = req.getContentType();
        if (contentType == null)
            return false;
        int separator = contentType.indexOf(';');
        String mediaType = separator >= 0
                ? contentType.substring(0, separator)
                : contentType;
        return "multipart/form-data".equalsIgnoreCase(mediaType.trim());
    }

    private static String extractFilename(String submittedFilename) {
        String normalized = submittedFilename.replace('\\', '/');
        int lastSlash = normalized.lastIndexOf('/');
        return normalized.substring(lastSlash + 1);
    }

    private static String extractExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot <= 0 || lastDot == filename.length() - 1)
            return "";
        return filename.substring(lastDot + 1).toLowerCase(Locale.ROOT);
    }

    private static byte[] readBytes(Part part) throws IOException {
        try (InputStream input = part.getInputStream();
                ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = input.read(buffer)) != -1) {
                output.write(buffer, 0, count);
            }
            return output.toByteArray();
        }
    }

    public static MapFile[] buildMapFiles(HttpServletRequest req, String name)
            throws IOException, ServletException {

        List<MapFile> files = new ArrayList<>();
        if (!isMultipart(req))
            return new MapFile[0];

        for (Part part : req.getParts()) {
            if (!name.equals(part.getName()))
                continue; // autre champ du formulaire

            String submitted = part.getSubmittedFileName();
            if (submitted == null || submitted.isEmpty())
                continue; // champ texte ou aucun fichier choisi

            String filename = extractFilename(submitted);
            String extension = extractExtension(filename);
            files.add(new MapFile(filename, extension, readBytes(part)));
        }
        return files.toArray(new MapFile[0]);
    }
}