/******************************************************************************* 
 * Copyright (c) 2013 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.jst.text.ext.test.HyperlinkTestUtil;
import org.jboss.tools.jst.text.ext.test.HyperlinkTestUtil.TestHyperlink;
import org.jboss.tools.jst.text.ext.test.HyperlinkTestUtil.TestRegion;
import org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.JQueryMobileHyperlinkDetector;
import org.jboss.tools.jst.web.ui.internal.text.ext.hyperlink.OpenWithEditorHyperlink;

public class JQueryMobileHyperlinkDetectorTest extends TestCase {
	private static final String PROJECT_NAME = "OpenOnTest";
	private static final String PAGE_NAME =  "/WebContent/jquery.html";
	
	public IProject project = null;

	protected void setUp() {
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(
				PROJECT_NAME);
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}
	
	protected void tearDown() {
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
	}

	public JQueryMobileHyperlinkDetectorTest() {
		super("jQuery Mobile Hyperlink Detector OpenOn on <div data-role=\"page\"> test");
	}
	
	public void testHyperlinkDetector() throws Exception{
		ArrayList<TestRegion> regionList = new ArrayList<TestRegion>();
		regionList.add(new TestRegion("<div data-role=\"page\" id=\"home\"", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#home' With BrowserSim")
		}));
		regionList.add(new TestRegion("</div", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#home' With BrowserSim")
		}));

		regionList.add(new TestRegion("<div data-role=\"page\" id=\"gallery\"", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#gallery' With BrowserSim")
		}));
		regionList.add(new TestRegion("</div", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#gallery' With BrowserSim")
		}));

		regionList.add(new TestRegion("<div data-role=\"page\" id=\"index\"", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#index' With BrowserSim")
		}));
		regionList.add(new TestRegion("</div", new TestHyperlink[]{
			new TestHyperlink(OpenWithEditorHyperlink.class, "Open 'jquery.html#index' With BrowserSim")
		}));

		
		HyperlinkTestUtil.checkRegions(project, PAGE_NAME, regionList, new JQueryMobileHyperlinkDetector(), true);
	}
}
