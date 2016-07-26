package net.simplelib;

import api.simplelib.lang.Local;
import com.google.common.collect.Maps;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.Map;

/**
 * @author ci010
 */
public class ModRestriction
{
	private static Type type;
	private static Map<String, String> map = Maps.newHashMap();

	public static void preInit(FMLPreInitializationEvent event)
	{
		if (event.getSide().isServer())
		{
			File cfgDir = event.getModConfigurationDirectory();
			Configuration cfg = new Configuration(new File(cfgDir, "ModRestriction.cfg"));
			cfg.load();
			String mode = cfg.get(Local.trans("cfg.restrict.mode"),
					Local.trans("cfg.restrict.mode.limit"),
					"BlackList",
					Local.trans("cfg.restrict.mode.limit.comment"))
					.getString();
			try
			{
				type = Type.valueOf(mode.toLowerCase());
			}
			catch (Exception e)
			{
				type = Type.blacklist;
			}
			if (type == Type.weakwhitelist || type == Type.whitelist)
			{
				map.put("mcp", "*");
				map.put("FML", "*");
				map.put("Forge", "*");
				map.put("helper", "*");
			}

			String src = cfg.get(Local.trans("cfg.restrict.mode"), Local.trans("cfg.restrict.mode.source"), "UpdateList",
					Local.trans("cfg.restrict.mode.source.comment"))
					.getString();
			src = src.toLowerCase();
			if (src.equals("mod"))
				for (ModContainer modContainer : Loader.instance().getActiveModList())
					map.put(modContainer.getModId(), "*");
			else
			{
				String[] lst = cfg.get(Local.trans("cfg.restrict.range"), Local.trans("cfg.restrict.range")
						, new String[]{"xRay *"}, Local.trans("cfg.restrict.range.comment"))
						.getStringList();
				for (String s : lst)
				{
					String[] split = s.split(" ");
					map.put(split[0], split[1]);
				}
			}
			cfg.save();
		}
	}

	public static boolean acceptModList(Map<String, String> modList, Side side)
	{
		if (side.isClient())
			if (type != null)
				return type.handle(modList);
		return true;
	}

	enum Type
	{
		whitelist
				{
					@Override
					boolean handle(Map<String, String> modList)
					{
						if (modList.size() != ModRestriction.map.size())
							return false;
						for (Map.Entry<String, String> entry : modList.entrySet())
						{
							String modid = entry.getKey(), version = entry.getValue();
							String availableVersion = ModRestriction.map.get(modid);
							if (availableVersion == null)
								return false;
							else if (!availableVersion.equals("*") && !availableVersion.equals(version))
								return false;
						}
						return true;
					}
				},
		blacklist
				{
					@Override
					boolean handle(Map<String, String> modList)
					{
						for (Map.Entry<String, String> entry : modList.entrySet())
						{
							String modid = entry.getKey(), version = entry.getValue();
							String availableVersion = ModRestriction.map.get(modid);
							if (availableVersion != null)
								if (availableVersion.equals("*") || availableVersion.equals(version))
									return false;
						}
						return true;
					}
				},
		weakwhitelist
				{
					@Override
					boolean handle(Map<String, String> modList)
					{
						for (Map.Entry<String, String> entry : modList.entrySet())
						{
							String modid = entry.getKey(), version = entry.getValue();
							String availableVersion = ModRestriction.map.get(modid);
							if (availableVersion == null)
								return false;
							else if (!availableVersion.equals("*") && !availableVersion.equals(version))
								return false;
						}
						return true;
					}
				};


		abstract boolean handle(Map<String, String> modList);
	}

}
