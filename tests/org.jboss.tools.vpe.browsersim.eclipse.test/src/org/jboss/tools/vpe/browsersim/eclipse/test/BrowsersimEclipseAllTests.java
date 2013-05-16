package org.jboss.tools.vpe.browsersim.eclipse.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowsersimEclipseAllTests extends TestSuite {
	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for BrowserSim Eclipse integration"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTestSuite(StubTest.class);
		//$JUnit-END$
		return suite;
	}
}
