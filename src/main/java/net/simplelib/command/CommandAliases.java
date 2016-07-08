package net.simplelib.command;

import api.simplelib.Instance;
import api.simplelib.LoadingDelegate;
import com.google.common.collect.Maps;
import net.minecraft.command.ICommand;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.simplelib.common.DebugLogger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author ci010
 */
@LoadingDelegate
public class CommandAliases
{
	@Instance
	public static CommandAliases INSTANCE = new CommandAliases();

	private Map<String, String[]> aliasesMap = Maps.newHashMap();

	private Configuration cfg;

	public void register(String cmd, String... aliases)
	{
		if (aliases == null || aliases.length == 0)
			return;
		aliasesMap.put(cmd, aliases);
	}

	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent event)
	{
		cfg = new Configuration(new File(event.getModConfigurationDirectory(),
				"command_aliases.cfg"));
	}

	@Mod.EventHandler
	public void serverStart(FMLServerAboutToStartEvent event)
	{
		Map<String, String[]> aliasesMap = Maps.newHashMap(this.aliasesMap);
		Map<String, ICommand> commands = event.getServer().getCommandManager().getCommands();
		for (Map.Entry<String, ICommand> entry : commands.entrySet())
		{
			List<String> list = entry.getValue().getCommandAliases();
			String[] aliases = cfg.get("aliases", entry.getKey(), list.toArray(new String[list.size()])).getStringList();
			if (aliases != null && aliases.length > 0)
				aliasesMap.put(entry.getKey(), aliases);
		}
		for (Map.Entry<String, String[]> entry : aliasesMap.entrySet())
			if (commands.containsKey(entry.getKey()))
				for (String s : entry.getValue())
					if (!commands.containsKey(s))
						commands.put(s, commands.get(entry.getKey()));
					else
						DebugLogger.fatal("Cannot add aliases [{}] to command [{}] as duplicated command name!",
								s, entry.getKey());
	}
}
