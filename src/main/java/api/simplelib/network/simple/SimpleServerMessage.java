package api.simplelib.network.simple;

import api.simplelib.seril.ITagSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ci010
 */
public abstract class SimpleServerMessage<T> extends NBTServerMessage
{
	public SimpleServerMessage()
	{
		super();
	}

	public SimpleServerMessage(T data)
	{
		this();
		delegate.set(getSerializer().serialize(data));
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
	{
		return this.handlerServerMessage(player, data == null ? null : getSerializer().deserialize(data));
	}

	public abstract ITagSerializer<T> getSerializer();

	public abstract IMessage handlerServerMessage(EntityPlayer player, T data);
}
