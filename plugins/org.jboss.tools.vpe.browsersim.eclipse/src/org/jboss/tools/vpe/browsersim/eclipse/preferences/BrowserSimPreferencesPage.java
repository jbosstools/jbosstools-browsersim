/*******************************************************************************
 * Copyright (c) 2007-2013 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.vpe.browsersim.eclipse.preferences;

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.dialogs.PreferenceLinkArea;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.Messages;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
public class BrowserSimPreferencesPage extends PreferencePage implements IWorkbenchPreferencePage{
	public static final String BROWSERSIM_JVM_ID = "org.jboss.tools.vpe.browsersim.jvm"; //$NON-NLS-1$
	public static final String BROWSERSIM_JVM_AUTOMATICALLY = "org.jboss.tools.vpe.browsersim.jvm.automatically"; //$NON-NLS-1$
	public static final String BROWSERSIM_GTK_2 = "org.jboss.tools.vpe.browsersim.gtk2"; //$NON-NLS-1$
	
	
	private Combo combo;
	private Button automatically;
	private Button select;
	private Button gtk2;
	private Button gtk3;
	
	private List<IVMInstall> jvms;
	
	private SelectionListener radioListener; 
	
	@Override
	public void init(IWorkbench workbench) {
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		jvms = PreferencesUtil.getSuitableJvms();
	}
	
	@Override
	protected Control createContents(Composite parent) {
		initializeDialogUnits(parent);
		int nColumns = 2;

		Composite result = new Composite(parent, SWT.NONE);
		result.setFont(parent.getFont());

		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.marginWidth = 0;
		layout.numColumns = nColumns;
		result.setLayout(layout);
		
		PreferenceLinkArea contentTypeArea = new PreferenceLinkArea(result, SWT.NONE,
				"org.eclipse.jdt.debug.ui.preferences.VMPreferencePage", Messages.BrowserSimPreferencesPage_JRE_LINK,//$NON-NLS-1$
				(IWorkbenchPreferenceContainer) getContainer(), null);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		contentTypeArea.getControl().setLayoutData(data);
		contentTypeArea.getControl().setFont(parent.getFont());

		Group jreGroup= new Group(result, SWT.NONE);
		
		GridLayout jreGroupLayout= new GridLayout();
		jreGroupLayout.numColumns= 3;
		jreGroup.setLayout(jreGroupLayout);
		
		GridData jreGroupLayoutData= new GridData(GridData.FILL_HORIZONTAL);
		jreGroupLayoutData.horizontalSpan= 2;
		jreGroup.setLayoutData(jreGroupLayoutData);
		
		jreGroup.setText(Messages.BrowserSimPreferencesPage_SELECT_JRE);
		
		radioListener = new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				controlChanged(e.widget);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		
		automatically = addRadioButton(jreGroup, Messages.BrowserSimPreferencesPage_DETECT_AUTOMATICALLY, BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.TRUE, 0);
		automatically.addSelectionListener(radioListener);
		select = addRadioButton(jreGroup, Messages.BrowserSimPreferencesPage_SELECT, BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.FALSE, 0);
		select.addSelectionListener(radioListener);
		
		combo = new Combo(jreGroup, SWT.READ_ONLY);
		for(int i = 0; i < jvms.size(); i++) {
			combo.add(jvms.get(i).getName(), i);
		}
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		fillValues();
		
		if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {
			addGtkGroup(result);
		}
		
		Dialog.applyDialogFont(result);
		return result;
	}
	
	private void addGtkGroup(Composite result) {
		Group gtkGroup = new Group(result, SWT.NONE);

		GridLayout gtkGroupLayout = new GridLayout();
		gtkGroupLayout.numColumns = 2;
		gtkGroup.setLayout(gtkGroupLayout);

		GridData gtkGridData = new GridData(GridData.FILL_HORIZONTAL);
		gtkGroup.setLayoutData(gtkGridData);

		gtkGroup.setText(Messages.BrowserSimPreferencesPage_SELECT_GTK);

		gtk2 = addRadioButton(gtkGroup, Messages.BrowserSimPreferencesPage_GTK_2, BROWSERSIM_GTK_2, IPreferenceStore.TRUE, 0);
		gtk3 = addRadioButton(gtkGroup, Messages.BrowserSimPreferencesPage_GTK_3, BROWSERSIM_GTK_2, IPreferenceStore.FALSE, 0);		
		
		setGtkValues();
	}

	private void setGtkValues() {
		if (IPreferenceStore.TRUE.equals(getPreferenceStore().getString(BROWSERSIM_GTK_2))) {
			gtk2.setSelection(true);
		} else {
			gtk3.setSelection(true);
		}
	}

	private Button addRadioButton(Composite parent, String label, String key, String value, int indent) {
		GridData gd= new GridData();
		gd.horizontalSpan= 2;
		gd.horizontalIndent= indent;

		Button button= new Button(parent, SWT.RADIO);
		button.setText(label);
		button.setData(new String[] { key, value });
		button.setLayoutData(gd);

		return button;
	}
	
	private void fillValues() {
		if (jvms.isEmpty()) {
			/*
			 * browserSim can be executed only on 32bit jvm on windows, that's why jvm list can be empty.
			 * @see https://issues.jboss.org/browse/JBIDE-13988
			 */
			String message;
			String os = Platform.getOS();
			boolean is32bitEclipse = PlatformUtil.ARCH_X86.equals(PlatformUtil.getArch());
			if (Platform.OS_WIN32.equals(os)) {
				message = Messages.BrowserSimPreferencesPage_REQUIREMENTS_WINDOWS;
			} else if (PlatformUtil.OS_MACOSX.equals(os) && is32bitEclipse) {
				message = Messages.BrowserSimPreferencesPage_REQUIREMENTS_MAC32;
			} else {// Linux, 64-bit Mac
				message = MessageFormat.format(Messages.BrowserSimPreferencesPage_REQUIREMENTS, is32bitEclipse ? "32-bit" : "64-bit"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			setMessage(message, IMessageProvider.ERROR);
			automatically.setSelection(true);
			select.setEnabled(false);
			combo.setEnabled(false);
		} else {
			int selectionIndex = -1;
			if (IPreferenceStore.FALSE.equals(getPreferenceStore().getString(BROWSERSIM_JVM_AUTOMATICALLY))) {
				String selectedID = getPreferenceStore().getString(BROWSERSIM_JVM_ID);
				for(int i = 0; i < jvms.size(); i++) {
					if (selectedID.equals(jvms.get(i).getId())) {
						selectionIndex = i;
						break;
					}
				}
				select.setSelection(true);
				controlChanged(select);
			} else {
				automatically.setSelection(true);
				controlChanged(automatically);
			}
			
			automatically.setText(automatically.getText() + " (" + jvms.get(0).getName() + ")"); //$NON-NLS-1$ //$NON-NLS-2$
			combo.select(selectionIndex);
		}
	}
	
	private void controlChanged(Widget widget) {
		if (select.equals(widget)) {
			combo.setEnabled(true);
			if (combo.getSelectionIndex() == -1) {
				combo.select(0);
			}
		} else {
			combo.setEnabled(false);
		}
	}
		
	@Override
	public boolean performOk() {
		IPreferenceStore store = getPreferenceStore();
		
		if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) {
			if (gtk2.getSelection()) {
				store.setValue(BROWSERSIM_GTK_2, IPreferenceStore.TRUE);
			} else {
				store.setValue(BROWSERSIM_GTK_2, IPreferenceStore.FALSE);
			}
		}
		
		if (automatically.getSelection()) {
			store.setValue(BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.TRUE);
		} else {
			store.setValue(BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.FALSE);
			String value = jvms.get(combo.getSelectionIndex()).getId();
			
			// check that this jvm was not deleted from Installed JRE's page before saving
			boolean exists = false;
			for (IVMInstall vm : PreferencesUtil.getSuitableJvms()) {
				if (value.equals(vm.getId())) {
					exists = true;
					break;
				}
			}
			
			if (exists) {
				store.setValue(BROWSERSIM_JVM_ID, value);
			} else {
				// if selected jvm is deleted from Installed JRE's page, any suitable jvm will use
				store.setValue(BROWSERSIM_JVM_AUTOMATICALLY, IPreferenceStore.TRUE);
			}
		}
		return true;
	}
	
	@Override
	protected void performDefaults() {
		automatically.setSelection(true);
		select.setSelection(false);
		
		if (PlatformUtil.OS_LINUX.equals(PlatformUtil.getOs())) { 
			gtk2.setSelection(true);
			gtk3.setSelection(false);
		}
		
		controlChanged(automatically);
		super.performDefaults();
	}
}
