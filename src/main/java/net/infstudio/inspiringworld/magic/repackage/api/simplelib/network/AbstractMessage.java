package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network;

import io.netty.buffer.ByteBuf;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.HelperMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

abstract class AbstractMessage<T> implements IMessageHandler<AbstractMessage<T>, IMessage>, IMessage
{
	protected final MessageCoder<T> delegate;

	public AbstractMessage(MessageCoder<T> coder)
	{
		this.delegate = coder;
	}

	public abstract IMessage handleClientMessage(EntityPlayer player, T data, MessageContext ctx);

	public abstract IMessage handleServerMessage(EntityPlayer player, T data, MessageContext ctx);

	@Override
	public IMessage onMessage(AbstractMessage<T> message, MessageContext ctx)
	{
		if (ctx.side.isClient())
			return handleClientMessage(HelperMod.proxy.getClientPlayer(), message.delegate.get(), ctx);
		else
			return handleServerMessage(ctx.getServerHandler().playerEntity, message.delegate.get(), ctx);
	}

	@SideOnly(Side.CLIENT)
	private EntityPlayer getSinglePlayer()
	{
		return Minecraft.getMinecraft().thePlayer;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		if (delegate != null)
			delegate.toBytes(buf);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		if (delegate != null)
			delegate.fromBytes(buf);
	}
}
