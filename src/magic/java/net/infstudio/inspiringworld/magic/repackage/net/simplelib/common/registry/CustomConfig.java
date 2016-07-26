package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry;

import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.Map;

/**
 * @author ci010
 */
class CustomConfig
{
	//TODO complete this class
	Map<String, Map<String, Configuration>> map = Maps.newHashMap();

	File root;

	public Configuration registerConfig(String name)
	{
		return new Configuration(new File(root, name));
	}

	class ConfigCollection
	{
		String modid, name;
	}
}
