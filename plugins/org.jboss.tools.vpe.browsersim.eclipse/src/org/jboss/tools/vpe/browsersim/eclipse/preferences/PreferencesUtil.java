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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.Launch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.jdt.internal.launching.LaunchingPlugin;
import org.eclipse.jdt.internal.launching.StandardVMType;
import org.eclipse.jdt.launching.AbstractVMInstall;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.vpe.browsersim.browser.PlatformUtil;
import org.jboss.tools.vpe.browsersim.eclipse.Activator;
import org.jboss.tools.vpe.browsersim.eclipse.launcher.ExternalProcessLauncher;

/**
 * @author Konstantin Marmalyukov (kmarmaliykov)
 */
@SuppressWarnings("restriction")
public class PreferencesUtil {
	/**
	 * @since 3.3 OSX environment variable specifying JRE to use
	 */
	protected static final String JAVA_JVM_VERSION = "JAVA_JVM_VERSION"; //$NON-NLS-1$
	
	public static List<IVMInstall> getSuitableJvms() {
		String eclipseOs = PlatformUtil.getOs();
		String eclipseArch = PlatformUtil.getArch();
		
		List<IVMInstall> vms = new ArrayList<IVMInstall>();
		for(IVMInstallType type : JavaRuntime.getVMInstallTypes()) {
			for(IVMInstall vm : type.getVMInstalls()) {
				if (vm instanceof AbstractVMInstall) {
					AbstractVMInstall abstractVm = (AbstractVMInstall) vm;
					String javaVersion = abstractVm.getJavaVersion();
					if (javaVersion != null && javaVersion.compareTo("1.6") >= 0) {
						if (PlatformUtil.OS_WIN32.equals(eclipseOs)) {
							// can use only 32 bit jvm on windows
							if (isX86(abstractVm) && !conflictsWithWebKit(abstractVm)) {
								vms.add(abstractVm);
							}
						} else if (PlatformUtil.OS_MACOSX.equals(eclipseOs)) {
							if (PlatformUtil.ARCH_X86.equals(eclipseArch)) {
								// Only Java 6 supports 32-bit mode on Mac
								if (javaVersion.startsWith("1.6")) {
									vms.add(abstractVm);
								}
							} else {
								vms.add(abstractVm);
							}
						} else if (PlatformUtil.OS_LINUX.equals(eclipseOs)) {
							if (PlatformUtil.ARCH_X86.equals(eclipseArch) == isX86(abstractVm)) {
								vms.add(abstractVm);
							}
						}
					}
				}
			}
		}
		
		return vms;
	}
	
	public static String getJVMPath(String id) {
		for(IVMInstallType type : JavaRuntime.getVMInstallTypes()) {
			for(IVMInstall vm : type.getVMInstalls()) {
				if (id.equals(vm.getId())) {
					return vm.getInstallLocation().getAbsolutePath();
				}
			}
		}
		return "";
	}
	
	public static String getArchitecture(IVMInstall defaultVMInstall) {
		try {
			return generateLibraryInfo(defaultVMInstall.getInstallLocation(), getJavaExecutable(defaultVMInstall));
		} catch (IOException e) {
			Activator.logError(e.getMessage(), e);
		}
		return null;
	}
	
	private static boolean isX86(IVMInstall vm) {
		return PlatformUtil.ARCH_X86.equals(PreferencesUtil.getArchitecture(vm));
	}
	
	private static boolean conflictsWithWebKit(IVMInstall vm) {
		File libxml2 = new File(vm.getInstallLocation().getAbsolutePath() + "/bin/libxml2.dll");
		return libxml2.exists();
	}
	
	private static File getJavaExecutable(IVMInstall standardVM) {
    	File installLocation = standardVM.getInstallLocation();
        if (installLocation != null) {
            return StandardVMType.findJavaExecutable(installLocation);
        }
        return null;
    }  
	
	/**
	 * @see org.eclipse.jdt.internal.launching.StandardVMType#generateLibraryInfo
	 */
	@SuppressWarnings({ "unchecked" })
	public static String generateLibraryInfo(File javaHome, File javaExecutable) throws IOException {
		String arch = null;
		String javaExecutablePath = javaExecutable.getAbsolutePath();
		String currentBundleLocation = ExternalProcessLauncher
				.getBundleLocation(Platform.getBundle(Activator.PLUGIN_ID));
		String[] cmdLine = new String[] {javaExecutablePath, "-cp", currentBundleLocation, "org.jboss.tools.vpe.browsersim.eclipse.preferences.ArchitectureDetector" }; //$NON-NLS-1$ //$NON-NLS-2$
		Process p = null;
		try {
			String envp[] = null;
			if (Platform.OS_MACOSX.equals(Platform.getOS())) {
				Map<String, String> map = DebugPlugin.getDefault().getLaunchManager().getNativeEnvironmentCasePreserved();
				if (map.remove(JAVA_JVM_VERSION) != null) {
					envp = new String[map.size()];
					Iterator<Entry<String, String>> iterator = map.entrySet().iterator();
					int i = 0;
					while (iterator.hasNext()) {
						Entry<String, String> entry = iterator.next();
						envp[i] = entry.getKey() + "=" + entry.getValue(); //$NON-NLS-1$
						i++;
					}
				}
			}
			p = DebugPlugin.exec(cmdLine, null, envp);
			IProcess process = DebugPlugin.newProcess(new Launch(null, ILaunchManager.RUN_MODE, null), p, "Library Detection"); //$NON-NLS-1$
			for (int i = 0; i < 600; i++) {
				// Wait no more than 30 seconds (600 * 50 milliseconds)
				if (process.isTerminated()) {
					break;
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			}
			arch = getArchitecture(process);
		} catch (CoreException ioe) {
			LaunchingPlugin.log(ioe);
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		if (arch == null) {
		    // log error that we were unable to generate library information - see bug 70011
		    LaunchingPlugin.log(NLS.bind("Failed to retrieve default libraries for {0}", new String[]{javaHome.getAbsolutePath()})); //$NON-NLS-1$
		}
		return arch;
	}

	private static String getArchitecture(IProcess process) {
		IStreamsProxy streamsProxy = process.getStreamsProxy();
		String text = null;
		if (streamsProxy != null) {
			text = streamsProxy.getOutputStreamMonitor().getContents();
		}
		if (text != null && text.length() > 0) {
			return PlatformUtil.parseArch(text);
		} 
		return null;
	}
}
