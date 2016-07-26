package api.simplelib.network.simple;

import api.simplelib.network.AbstractServerMessage;
import api.simplelib.network.NBTCoder;
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
