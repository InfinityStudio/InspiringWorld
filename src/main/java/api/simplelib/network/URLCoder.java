package api.simplelib.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ci010
 */
public class URLCoder implements MessageCoder<URL>
{
	private URL url;

	@Override
	public void set(URL value)
	{
		this.url = value;
	}

	@Override
	public URL get()
	{
		return url;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		String s = ByteBufUtils.readUTF8String(buf);
		try
		{
			url = new URL(s);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		ByteBufUtils.writeUTF8String(buf, url.toString());
	}
}
