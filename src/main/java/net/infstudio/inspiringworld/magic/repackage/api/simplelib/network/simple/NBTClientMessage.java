package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.simple;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.AbstractClientMessage;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.NBTCoder;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public abstract class NBTClientMessage extends AbstractClientMessage<NBTTagCompound>
{
	public NBTClientMessage()
	{
		super(new NBTCoder());
	}
}
