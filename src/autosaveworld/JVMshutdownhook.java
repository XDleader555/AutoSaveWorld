/**
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 3
* of the License, or (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
*
*/

package autosaveworld;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

public class JVMshutdownhook extends Thread {

	private String crashrestartscriptpath = "start.sh"; 
	private boolean chwdir = false;
	private String wdir = "";
	
	public void setPath(String path)
	{
		this.crashrestartscriptpath = path;
	}
	
	public void setWDir(boolean chwdir, String wdir)
	{
		this.chwdir = chwdir;
		this.wdir = wdir;
	}
	
	public void restart()
	{
	try {
		File restartscript = new File(crashrestartscriptpath);
		String OS = System.getProperty("os.name").toLowerCase();
		if (restartscript.exists()) {
			System.out.println("[AutoSaveWorld] Startup script found. Restarting");	
			if (OS.contains("win")) {
				Runtime.getRuntime().exec("cmd /c start " + restartscript.getCanonicalPath());
			} else {
				Runtime.getRuntime().exec(restartscript.getCanonicalPath());
			}
		} else {
			System.out.println("[AutoSaveWorld] Startup script not found. Restarting without it. This may work strange or not work at all");
			//requred info for start script
			String jarfilename = Bukkit.class.getResource("").getFile();
			jarfilename = jarfilename.substring(0, jarfilename.indexOf(".jar"));
			jarfilename = new File(jarfilename).getName()+".jar";
			List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();			
			//start script building
			List<String> execsequence = new ArrayList<String>();
			execsequence.add("java");
			execsequence.addAll(arguments);
			execsequence.add("-jar");
			execsequence.add(jarfilename);
			
			//build additional bukkit parameters
			if (chwdir) {
				execsequence.add("-W");
				execsequence.add(wdir);
			}
			
			ProcessBuilder pb = new ProcessBuilder();
			pb.command(execsequence);
			pb.start();

			}
	} catch (Exception e)
	{System.out.println("[AutoSaveWorld] Restart failed");
	e.printStackTrace();}
	}
	
	
	public void run()
	{
		restart();
	}
	
}
