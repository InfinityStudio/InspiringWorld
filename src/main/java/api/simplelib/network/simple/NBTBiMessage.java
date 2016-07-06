package api.simplelib.network.simple;

import api.simplelib.network.AbstractBiMessage;
import api.simplelib.network.NBTCoder;
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
