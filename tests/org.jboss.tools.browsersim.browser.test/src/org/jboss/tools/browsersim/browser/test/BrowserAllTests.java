package org.jboss.tools.browsersim.browser.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Yahor Radtsevich
 */
public class BrowserAllTests extends TestSuite {
	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for BrowserSim components"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(WebKitTests.class);
		//$JUnit-END$
		return suite;
	}
}
