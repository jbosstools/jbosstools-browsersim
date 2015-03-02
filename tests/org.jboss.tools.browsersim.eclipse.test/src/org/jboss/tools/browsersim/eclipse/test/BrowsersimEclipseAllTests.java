package org.jboss.tools.browsersim.eclipse.test;

import org.jboss.tools.test.util.ProjectImportTestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowsersimEclipseAllTests extends TestSuite {
	public static Test suite() {

		TestSuite suite = new TestSuite("Tests for BrowserSim Eclipse integration"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		
		suite.addTest(new ProjectImportTestSetup(new TestSuite(JQueryMobileHyperlinkDetectorTest.class),
				"org.jboss.tools.browsersim.eclipse.test",
				new String[]{"projects/OpenOnTest"},
				new String[]{"OpenOnTest"}));
		
		//$JUnit-END$
		return suite;
	}
}
