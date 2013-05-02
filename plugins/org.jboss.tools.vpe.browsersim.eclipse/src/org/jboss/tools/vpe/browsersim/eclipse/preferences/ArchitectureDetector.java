package org.jboss.tools.vpe.browsersim.eclipse.preferences;

public class ArchitectureDetector {
	/**
	 * Prints system properties to standard out.
	 * <ul>
	 * <li>os.arch</li>
	 * </ul>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print(System.getProperty("os.arch")); //$NON-NLS-1$
	}
}