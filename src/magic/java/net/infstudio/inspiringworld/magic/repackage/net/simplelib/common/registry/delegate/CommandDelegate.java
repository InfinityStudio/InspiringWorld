package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.delegate;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.LoadingDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.lang.Local;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ASMRegistryDelegate;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.command.ISimpleCommand;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.command.ModCommand;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraft.command.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author ci010
 */
@LoadingDelegate
public class CommandDelegate extends ASMRegistryDelegate<ModCommand>
{
	@Mod.EventHandler
	public void onServerStart(FMLServerAboutToStartEvent event)
	{
		try
		{
			final Object o = this.getAnnotatedClass().newInstance();
			ICommand cmd;
			if (o instanceof ICommand)
				cmd = (ICommand) o;
			else if (o instanceof ISimpleCommand)
				cmd = new CommandBase()
				{
					@Override
					public String getCommandName()
					{
						return ((ISimpleCommand) o).name();
					}

					@Override
					public String getCommandUsage(ICommandSender sender)
					{
						return "commands.".concat(this.getCommandName()).concat(".usage");
					}

					@Override
					public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
					{
						((ISimpleCommand) o).processCommand(sender, args);

					}
				};
			else
			{
				return;
			}
			final String commandUsage = cmd.getCommandUsage(null);
			if (commandUsage != null)
				Local.trans(commandUsage);
			DebugLogger.info("Register the command [/{}] <- [{}:{}].", cmd.getCommandName(),
					this.getModid(), this.getAnnotatedClass());
			((ServerCommandManager) event.getServer().getCommandManager()).registerCommand(cmd);
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
	}
}
