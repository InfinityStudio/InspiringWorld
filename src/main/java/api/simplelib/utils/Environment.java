package api.simplelib.utils;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * @author ci010
 */
public class Environment
{
	private static boolean DEBUG;

	static
	{
		boolean fail = false;
		try
		{
			Class.forName("net.minecraftforge.gradle.GradleStartCommon");
		}
		catch (ClassNotFoundException e)
		{
			fail = true;
		}
		DEBUG = !fail;
	}

	@SideOnly(Side.CLIENT)
	public static void restartMC()
	{
		Runtime.getRuntime().addShutdownHook(new Restart());
		FMLCommonHandler.instance().exitJava(0, false);
	}

	@SideOnly(Side.CLIENT)
	private static class Restart extends Thread
	{
		@Override
		public void run()
		{
			try
			{
				String cmd;
				List<String> cmds = Lists.newArrayList();
				RuntimeMXBean bean = ManagementFactory.getRuntimeMXBean();
				String classPath = bean.getClassPath();
				List<String> vmOptions = bean.getInputArguments();
				String mainClass = System.getProperty("sun.java.command");

//				Runtime.getRuntime().exec("java " + Joiner.on(' ').join(vmOptions) + " -cp " + classPath + " " + mainClass);
				cmd = "start " + "\"" + System.getProperty("java.home") + File.separator + "bin" + File.separator +
						"java\"";
				cmds.add("start");
				cmds.add(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java");
				for (String s : vmOptions)
				{
					if (s.contains(" "))
					{
						String[] split = s.split("=");
						if (split.length > 1)
						{
							split[1] = "\"" + split[1] + "\"";
							s = split[0] + split[1];
						}
					}
					if (s.contains("IDEA") || s.contains("idea"))
						continue;
					cmds.add(s);
					cmd = cmd + " " + s;
				}
				cmds.add("-cp");
				cmds.add(classPath);
				cmds.add(mainClass);
				cmd = cmd + " -cp \"" + classPath + "\" " + mainClass;
				System.out.println(cmd);
				Runtime.getRuntime().exec(cmd);
//				ProcessBuilder builder = new ProcessBuilder(cmds);
//				builder.start();

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static boolean debug()
	{
		return DEBUG;
	}
}
