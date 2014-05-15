package org.jboss.tools.vpe.browsersim.eclipse.dialog;

import java.text.MessageFormat;
import java.util.Iterator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.eclipse.Messages;

public class BrowserSimErrorDialog extends MessageDialog {
	private static final String BROWSERSIM_PREFERENCE_PAGE_ID = "org.jboss.tools.vpe.browsersim.eclipse.preferences.BrowserSimPreferences"; //$NON-NLS-1$
	
	private String programName;
	
	public BrowserSimErrorDialog(Shell parentShell, String dialogTitle,	Image dialogTitleImage,
			String programName, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, MessageFormat.format(Messages.BrowserSimErrorDialog_ERROR_MESSAGE_HEADER, programName),
				dialogImageType, dialogButtonLabels, defaultIndex);
		this.programName = programName;
	}

	@Override
	protected Control createCustomArea(final Composite parent) {
		Link link = new Link(parent, SWT.NONE);
		
		GridData data = new GridData();
        data.horizontalSpan = 2;
        link.setData(data);
		
		boolean is32bitEclipse = PlatformUtil.ARCH_X86.equals(PlatformUtil.getArch());
		IPreferenceNode jreNode = getPreferenceNode(BROWSERSIM_PREFERENCE_PAGE_ID);
		String bit = is32bitEclipse ? "32-bit" : "64-bit"; //$NON-NLS-1$ //$NON-NLS-2$
		String message = MessageFormat.format(Messages.BrowserSimErrorDialog_ERROR_MESSAGE_COMMON, programName, jreNode.getLabelText(), bit);

	    link.setText(message);
	    link.addSelectionListener(new SelectionAdapter(){
	        @Override
	        public void widgetSelected(SelectionEvent e) {
	        	close();
	        	PreferencesUtil.createPreferenceDialogOn(null, BROWSERSIM_PREFERENCE_PAGE_ID, new String[] {BROWSERSIM_PREFERENCE_PAGE_ID}, null).open();
	        }
	    });

		return link;
	}
	
	/**
     * Get the preference node with pageId.
     * 
     * @param pageId
     * @return IPreferenceNode
     */
    @SuppressWarnings("rawtypes")
	private IPreferenceNode getPreferenceNode(String pageId) {
    	Iterator iterator = PlatformUI.getWorkbench().getPreferenceManager()
                .getElements(PreferenceManager.PRE_ORDER).iterator();
        while (iterator.hasNext()) {
            IPreferenceNode next = (IPreferenceNode) iterator.next();
            if (next.getId().equals(pageId)) {
				return next;
			}
        }
        return null;
    }
}
