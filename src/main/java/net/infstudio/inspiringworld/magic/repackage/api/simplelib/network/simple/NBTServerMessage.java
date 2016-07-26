package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.simple;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.AbstractServerMessage;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.NBTCoder;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public abstract class NBTServerMessage extends AbstractServerMessage<NBTTagCompound>
{
	public NBTServerMessage()
	{
		super(new NBTCoder());
	}
}
