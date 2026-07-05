package mg.itu.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import mg.itu.annotation.controller.Controller;
import mg.itu.annotation.url.UrlMapping;

public class ClassScanner {

    // Méthode 1 : lister toutes les classes d'un package
    public static List<Class<?>> getClassesList(String packageName)
            throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<>();

        String path = packageName.replace('.', '/');
        File dir = new File(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource(path).getFile()
        );

        File[] files = dir.listFiles();
        if (files == null) {
            return classList;
        }

        for (File file : files) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "."
                        + file.getName().replace(".class", "");
                classList.add(Class.forName(className));
            }
        }
        return classList;
    }

    // Méthode 2 : filtrer par annotation de classe
    public static List<Class<?>> getClassesByAnnotation(
            String packageName, Class<? extends Annotation> annotation) throws ClassNotFoundException {
        List<Class<?>> result = new ArrayList<>();
        for (Class<?> clazz : getClassesList(packageName)) {
            if (clazz.isAnnotationPresent(annotation)) {
                result.add(clazz);
            }
        }
        return result;
    }

    // Méthode 3 : vérifier si une URL existe déjà dans la map
    public static boolean urlMethodExists(
            Map<UrlMethod, Method> map,
            String url,
            String httpMethod) {
        return map.containsKey(new UrlMethod(url, httpMethod));
    }

    // Méthode 4 : construire la map UrlMethod → Method
    public static Map<UrlMethod, Method> getUrlMethodMap(String packageName)
            throws ClassNotFoundException {
        Map<UrlMethod, Method> map = new HashMap<>();

        List<Class<?>> controllers = getClassesByAnnotation(
                packageName, Controller.class
        );

        for (Class<?> controller : controllers) {
            for (Method method : controller.getDeclaredMethods()) {

                UrlMapping urlMapping = method.getAnnotation(UrlMapping.class);

                if (urlMapping != null) {
                    String url = urlMapping.value();
                    String httpMethod = urlMapping.method().toUpperCase();

                    UrlMethod key = new UrlMethod(url, httpMethod);

                    // Vérifier les doublons
                    if (urlMethodExists(map, url, httpMethod)) {
                        throw new IllegalStateException(
                                "URL en double détectée : " + key
                        );
                    }

                    map.put(key, method);
                }
            }
        }
        return map;
    }
}
