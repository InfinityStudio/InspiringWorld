package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;

/**
 * @author ci010
 */
public interface ISimpleCommand
{
	String name();

	void processCommand(ICommandSender sender, String[] args) throws CommandException;

	interface NickName
	{
		String[] getNikeName();
	}

	class UsageException extends WrongUsageException
	{
		public UsageException(String cmdName)
		{
			this(cmdName, new Object[0]);
		}

		public UsageException(String cmdName, Object... replacements)
		{
			super("commands.".concat(cmdName).concat(".usage"), replacements);
		}
	}
}
