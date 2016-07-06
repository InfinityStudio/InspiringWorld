package api.simplelib.network.simple;

import api.simplelib.seril.ITagSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ci010
 */
public abstract class SimpleBiMessage<T> extends NBTBiMessage
{
	public SimpleBiMessage()
	{
		super();
	}

	public SimpleBiMessage(T data)
	{
		this();
		delegate.set(getSerializer().serialize(data));
	}

	@Override
	public IMessage handleClientMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
	{
		return handleClientMessage(player, data == null ? null : getSerializer().deserialize(data));
	}

	@Override
	public IMessage handleServerMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
	{
		return handlerServerMessage(player, data == null ? null : getSerializer().deserialize(data));
	}

	public abstract ITagSerializer<T> getSerializer();

	public abstract IMessage handleClientMessage(EntityPlayer player, T data);

	public abstract IMessage handlerServerMessage(EntityPlayer player, T data);
}
