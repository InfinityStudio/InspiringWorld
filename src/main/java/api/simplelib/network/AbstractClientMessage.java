package api.simplelib.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractClientMessage<T> extends AbstractMessage<T>
{
	public AbstractClientMessage(MessageCoder<T> coder)
	{
		super(coder);
	}

	@Override
	public final IMessage handleServerMessage(EntityPlayer player, T data, MessageContext ctx)
	{
		return null;
	}
}
