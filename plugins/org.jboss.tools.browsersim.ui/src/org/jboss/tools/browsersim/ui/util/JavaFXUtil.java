package org.jboss.tools.browsersim.ui.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.browsersim.ui.BrowserSimLogger;

public class JavaFXUtil {
	private static List<String> getJavaFXLibrariesPath(String javaHome) {
		List<String> libsPaths = new ArrayList<String>();
		//Java 7 and 9
		libsPaths.add(javaHome + File.separator + "lib" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		//Java 8 and 9		
		libsPaths.add(javaHome + File.separator + "lib" + File.separator + "jfxswt.jar"); //$NON-NLS-1$ //$NON-NLS-2$
		//Java 8
		libsPaths.add(javaHome + File.separator + "lib" + File.separator + "ext" + File.separator + "jfxrt.jar"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return libsPaths;
	}
	
	/**Checks if current JVM has JavaFX libraries and loads it into runtime.
	 * 
	 * @return true if JavaFX is available for current JVM
	 */
	public static boolean loadJavaFX() {
		return loadJavaFX(System.getProperty("java.home")); //$NON-NLS-1$
	}	
	
	public static boolean loadJavaFX(String javaHome) {
		boolean loaded = false;
		for (String libPath : getJavaFXLibrariesPath(javaHome)) {
			File lib = new File(libPath);
			if (lib.exists()) {
				loadJar(lib);
				loaded = true;
			}
		}
		return loaded;
	}
	
	public static boolean isJavaFXAvailable() {
		return isJavaFXAvailable(System.getProperty("java.home")); //$NON-NLS-1$
	}
	
	public static boolean isJavaFXAvailable(String javaHome) {
		for (String libPath : getJavaFXLibrariesPath(javaHome)) {
			File lib = new File(libPath);
			if (lib.exists()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isJavaFX8OrHigher() {
		String javaHome = System.getProperty("java.home"); //$NON-NLS-1$
		// Java 8+ has SWT classes separated from JavaFX runtime library
		return new File(javaHome + File.separator + "lib" + File.separator + "jfxswt.jar").exists(); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	/**Extracts jar file with mocks to temporal folder and load it to classpath.
	 * This method should be used only for Standalone BrowserSim.
	 * 
	 * @param tempDir - directory to extract jar file
	 * @param jarName - path to jar file
	 */
	public static void loadMock(String tempDir, String jarName) {
	    InputStream inputStream = null;
	    try {
			inputStream = BrowserSimUtil.class.getClassLoader().getResourceAsStream(jarName);
			Path tmpFile = Paths.get(tempDir, jarName);
			Files.copy(inputStream, tmpFile);
			loadJar(tmpFile.toFile());          
	    } catch (FileNotFoundException e) {
            BrowserSimLogger.logError(e.getMessage(), e);
        } catch (IOException e) {
            BrowserSimLogger.logError(e.getMessage(), e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    BrowserSimLogger.logError(e.getMessage(), e);
                }
            }
        }
        
    }
	
	private static void loadJar(File file) {
		try {
			URL u = file.toURI().toURL();

			URLClassLoader sysloader = (URLClassLoader) ClassLoader
					.getSystemClassLoader();
			@SuppressWarnings("rawtypes")
			Class sysclass = URLClassLoader.class;
			@SuppressWarnings("unchecked")
			Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class }); //$NON-NLS-1$
			method.setAccessible(true);
			method.invoke(sysloader, new Object[] { u });
		} catch (Throwable t) {
			BrowserSimLogger.logError("Unable to add " + file.getName() + " to classpath.", t); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
}
