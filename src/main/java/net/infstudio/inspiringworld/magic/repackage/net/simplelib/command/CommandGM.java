package net.infstudio.inspiringworld.magic.repackage.net.simplelib.command;

import com.google.common.collect.Lists;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.command.ModCommand;
import net.minecraft.command.CommandGameMode;

import java.util.List;

/**
 * @author ci010
 */
@ModCommand
public class CommandGM extends CommandGameMode
{
	@Override
	public List getCommandAliases()
	{
		return Lists.newArrayList("gm");
	}
}
