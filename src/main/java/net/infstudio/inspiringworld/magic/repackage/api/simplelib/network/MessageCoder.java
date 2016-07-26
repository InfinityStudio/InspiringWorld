package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network;

import io.netty.buffer.ByteBuf;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.vars.Var;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ci010
 */
public interface MessageCoder<T> extends IMessage, Var<T>
{
	MessageCoder<Void> EMPTY = new MessageCoder<Void>()
	{
		@Override
		public void fromBytes(ByteBuf buf) {}

		@Override
		public void toBytes(ByteBuf buf) {}

		@Override
		public Void get() {return null;}

		@Override
		public void set(Void value) {}
	};
}
