package api.simplelib.io.cache;

import api.simplelib.seril.IJsonSerializer;
import api.simplelib.seril.ITagSerializer;
import api.simplelib.seril.NBTTagBuilder;
import com.google.gson.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author ci010
 */
public class CacheInfo
{
	public static final ITagSerializer<CacheInfo> nbtSeril = new ITagSerializer<CacheInfo>()
	{
		@Override
		public NBTTagCompound serialize(CacheInfo data)
		{
			return NBTTagBuilder.create().addString("location", data.location.toString())
					.addString("url", data.url.toString())
					.addString("type", data.type)
					.addString("md5", data.md5).build();
		}

		@Override
		public CacheInfo deserialize(NBTTagCompound tag)
		{
			try
			{
				ResourceLocation location = new ResourceLocation(tag.getString("location"));
				URL url = new URL(tag.getString("url"));
				String md5 = tag.getString("md5");
				return new CacheInfo(location, url, md5, tag.getString("type"));
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			return null;
		}
	};

	private ResourceLocation location;
	private URL url;
	private String md5;
	private String type;

	public static CacheInfo from(NBTTagCompound tagCompound)
	{
		return nbtSeril.deserialize(tagCompound);
	}

	public static NBTTagCompound to(CacheInfo info)
	{
		return nbtSeril.serialize(info);
	}

	public CacheInfo() {}

	public CacheInfo(ResourceLocation location, URL url, String md5, String type)
	{
		this.location = location;
		this.url = url;
		this.md5 = md5;
		this.type = type;
	}

	public ResourceLocation location()
	{
		return location;
	}

	public URL url()
	{
		return url;
	}

	public String type()
	{return type;}

	public String md5()
	{
		return md5;
	}
}
