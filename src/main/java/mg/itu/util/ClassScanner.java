package mg.itu.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassScanner {

    public static List<Class<?>> getClassesList(String packageName) throws ClassNotFoundException {
        List<Class<?>> classList = new ArrayList<Class<?>>();

        String path = packageName.replace('.', '/');
        File dir = new File(
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResource(path).getFile()
        );

        if (dir.listFiles() == null) return classList;

        for (File file : dir.listFiles()) {
            if (file.getName().endsWith(".class")) {
                String className = packageName + "." +
                    file.getName().replace(".class", "");
                classList.add(Class.forName(className));
            }
        }

        return classList;
    }

    public static boolean isClassInPackage(Class<?> clazz, String packageName) {
        return clazz.getPackage().getName().equals(packageName);
    }

    public static List<Class<?>> getClassesByAnnotation(String packageName, Class<?> annotation) throws ClassNotFoundException {
        List<Class<?>> annotatedClasses = new ArrayList<Class<?>>();
        List<Class<?>> classes = getClassesList(packageName);

        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotation.asSubclass(java.lang.annotation.Annotation.class))) {
                annotatedClasses.add(clazz);
            }
        }

        return annotatedClasses;
    }
}
