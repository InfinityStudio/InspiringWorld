package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractServerMessage<T> extends AbstractMessage<T>
{
	public AbstractServerMessage(MessageCoder<T> coder)
	{
		super(coder);
	}

	@Override
	public final IMessage handleClientMessage(EntityPlayer player, T data, MessageContext ctx)
	{
		return null;
	}
}
