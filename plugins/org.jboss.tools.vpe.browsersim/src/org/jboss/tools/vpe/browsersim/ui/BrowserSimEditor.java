package org.jboss.tools.vpe.browsersim.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class BrowserSimEditor {
	public static void show(String pageSource) {
		Shell shell = new Shell(SWT.SHELL_TRIM);
	    shell.setLayout(new FillLayout());
	    			    
	    final Text text = new Text(shell, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL|SWT.FULL_SELECTION);
	    text.setText(pageSource);
	    text.addVerifyListener(new VerifyListener() {
	        public void verifyText(VerifyEvent event) {
	            event.doit = false;
	        };
	    });
	    
	    Menu bar = new Menu (shell, SWT.BAR);
		shell.setMenuBar (bar);
		MenuItem editItem = new MenuItem (bar, SWT.CASCADE);
		editItem.setText ("Edit");
		Menu submenu = new Menu (shell, SWT.DROP_DOWN);
		editItem.setMenu (submenu);

		MenuItem copyAllItem = new MenuItem (submenu, SWT.PUSH);
		copyAllItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				text.selectAll();
			}
		});
		copyAllItem.setText ("Select &All\tCtrl+A");
		copyAllItem.setAccelerator (SWT.MOD1 + 'A');
		
		MenuItem copyItem = new MenuItem (submenu, SWT.PUSH);
		copyItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event e) {
				text.copy();
			}
		});
		copyItem.setText ("Copy\tCtrl+C");
		copyItem.setAccelerator (SWT.MOD1 + 'C');
		
		
		// Note that as long as your application has not overridden 
		// the global accelerators for copy, paste, and cut 
		//(CTRL+C or CTRL+INSERT, CTRL+V or SHIFT+INSERT, and CTRL+X or SHIFT+DELETE)
		// these behaviours are already available by default.
		// If your application overrides these accelerators,
		// you will need to call Text.copy(), Text.paste() and Text.cut()
		// from the Selection callback for the accelerator when the 
		// text widget has focus.
	    
	    shell.open();
	}
}
