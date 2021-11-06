package helix.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Generic Class Utilities
 * @author Sly
 *
 */
public class ClassUtils {
	public static final String CLASS_PATH = new File("").getAbsolutePath() + "/src/main/java/main";

	/**
	 * For when running from a compiled jar file
	 * @return all distinct {@link Class}es in the compiled jar
	 * @throws IOException - if an IOException has occurred
	 */
	public static Set<Class<?>> getClassesFromJarFile() throws IOException {
		Set<Class<?>> clazzes = new HashSet<>();
		String path = getJarFile();
		System.out.println("Running from: " + path);
		File file = new File(path);		
	    Set<String> classNames = new HashSet<>();
	    try (JarFile jarFile = new JarFile(file)) {
	        Enumeration<JarEntry> e = jarFile.entries();
	        while (e.hasMoreElements()) {
	            JarEntry jarEntry = e.nextElement();
	            if (jarEntry.getName().endsWith(".class")) {
	                String className = jarEntry.getName()
	                  .replace("/", ".")
	                  .replace(".class", "");
	                classNames.add(className);
	                Class<?> clazz = Class.forName(className);
	                clazzes.add(clazz);
	                System.out.println(clazz.getCanonicalName());
	            }
	        }
	        return clazzes;
	    } catch (ClassNotFoundException e1) {
	    	System.err.println("Could not find class: " + e1.getCause());
			e1.printStackTrace();
			return null;
		}
	}
	
	private static String getJarFile() throws FileNotFoundException {
	    String path = ClassUtils.class.getResource(ClassUtils.class.getSimpleName() + ".class").getFile();
	    if(path.startsWith("/")) {
	        throw new FileNotFoundException("This is not a jar file: \n" + path);
	    }
	    path = ClassLoader.getSystemClassLoader().getResource(path).getFile();
	    path = path.replaceAll("file:/", "");
	    return path.substring(0, path.indexOf('!'));
	}
	
	public static Set<Class<?>> getClasses() {
		return getClasses(CLASS_PATH);
	}

	public static Set<Class<?>> getClasses(String path) {
		File currentDir = new File(path);
		Set<String> classNameList = new HashSet<String>();

		for (String p : currentDir.list())
			classNameList.addAll(getClasses(classNameList, path + "/" + p));

		Set<Class<?>> classList = new HashSet<Class<?>>();
		for (String className : classNameList) {
			try {
				classList.add(Class.forName(className));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

		return classList;
	}

	private static Set<String> getClasses(Set<String> classesList, String path) {
		File currentDir = new File(path);
		if (!currentDir.exists()) {
			System.err.println("premature return");
			return classesList;
		}

		if (currentDir.isDirectory()) {
			for (String p : currentDir.list()) {
				File next = new File(path + "/" + p);
				if (next.isDirectory()) {
					classesList.addAll(getClasses(classesList, path + "/" + p));
				} else {
					classesList.add(getFullyQualifiedName(path + "/" + p));
				}
			}

		} else if (!currentDir.isDirectory()) {
			String qualifiedName = getFullyQualifiedName(path);
			classesList.add(qualifiedName);
		}

		return classesList;
	}

	public static String getFullyQualifiedName(String path) {
		int index = path.indexOf("java/main");

		// Remove everything before incl. "java/" and then remove ".java" from the end
		path = path.substring(index).substring(5).replaceAll("/", ".");
		return path.substring(0, path.length() - 5);
	}
}
