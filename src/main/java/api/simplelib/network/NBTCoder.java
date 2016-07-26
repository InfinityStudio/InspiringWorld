package api.simplelib.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class NBTCoder implements MessageCoder<NBTTagCompound>
{
	private NBTTagCompound data;

	@Override
	public void fromBytes(ByteBuf buf)
	{
		data = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeTag(buf, data);
	}

	@Override
	public NBTTagCompound get()
	{
		return data;
	}

	@Override
	public void set(NBTTagCompound value)
	{
		data = value;
	}
}
