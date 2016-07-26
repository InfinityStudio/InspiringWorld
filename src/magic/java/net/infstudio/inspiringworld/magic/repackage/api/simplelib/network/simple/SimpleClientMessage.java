package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.simple;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril.ITagSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author ci010
 */
public abstract class SimpleClientMessage<T> extends NBTClientMessage
{
	public SimpleClientMessage()
	{
		super();
	}

	public SimpleClientMessage(T data)
	{
		this();
		delegate.set(getSerializer().serialize(data));
	}

	@Override
	public IMessage handleClientMessage(EntityPlayer player, NBTTagCompound data, MessageContext ctx)
	{
		return handleClientMessage(player, data == null ? null : this.getSerializer().deserialize(data));
	}

	public abstract ITagSerializer<T> getSerializer();

	public abstract IMessage handleClientMessage(EntityPlayer player, T data);
}
