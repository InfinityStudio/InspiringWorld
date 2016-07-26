package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.simple;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.AbstractBiMessage;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.NBTCoder;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public abstract class NBTBiMessage extends AbstractBiMessage<NBTTagCompound>
{
	public NBTBiMessage()
	{
		super(new NBTCoder());
	}
}
