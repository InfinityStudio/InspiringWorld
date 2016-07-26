package api.simplelib.network.simple;

import api.simplelib.network.AbstractClientMessage;
import api.simplelib.network.NBTCoder;
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
