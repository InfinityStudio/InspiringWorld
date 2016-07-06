package net.simplelib.common.registry;

import api.simplelib.utils.FileReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import api.simplelib.Instance;
import api.simplelib.registry.ModHandler;
import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author ci010
 */
@ModHandler
public class LanguageReporter
{
	@Instance
	private static LanguageReporter instance = new LanguageReporter();
	/**
	 * The support language type of the mod.
	 */
	private List<String> langNodes = Lists.newArrayList();
	/**
	 * The keys needed to be localized.
	 */
	private Set<File> fileLang = Sets.newHashSet();

	public void report(String unlocalizedName)
	{
//		if (!unlocalizedName.endsWith(".name"))
//			unlocalizedName = unlocalizedName.concat(".name=");
//		else
		unlocalizedName = unlocalizedName.concat("=");
		langNodes.add(unlocalizedName);
	}

	public LanguageReporter start(String modid, String[] str)
	{
		try
		{
			for (String name : str)
			{
				File f = new File(FileReference.getRefer(modid).dirLang, name.concat(".lang"));
				if (!f.exists())
					f.createNewFile();
				fileLang.add(f);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return this;
	}

	public void end()
	{
		try
		{
			for (File lang : fileLang)
			{
				BufferedWriter writer = new BufferedWriter(new FileWriterWithEncoding(lang, "UTF-8"));
				Collections.sort(langNodes);
				char last = 0;
				boolean init = false;
				for (String name : langNodes)
				{
					if (init && name.charAt(0) != last)
						writer.write("\n");
					last = name.charAt(0);
					init = true;
					writer.write(name);
					writer.newLine();
				}
				writer.flush();
				writer.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		this.langNodes.clear();
		this.fileLang.clear();
	}

//	@SubscribeEvent
//	public void exit(ClientStopEvent event)
//	{
//		if (this.langNodes.isEmpty())
//			return;
//		this.start("all", new String[]{"zh_CN", "en_US"});
//		this.end();
//	}

	public static LanguageReporter instance()
	{
		return instance;
	}

	void close()
	{
		instance = null;
	}
}
