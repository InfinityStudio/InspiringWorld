package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

/**
 * @author ci010
 */
public class StringCoder implements MessageCoder<String>
{
	private String string = "";

	@Override
	public void set(String value)
	{
		this.string = value;
	}

	@Override
	public String get()
	{
		return string;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		string = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, string);
	}
}
