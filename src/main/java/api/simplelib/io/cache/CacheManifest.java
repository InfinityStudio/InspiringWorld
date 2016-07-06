package api.simplelib.io.cache;

import api.simplelib.seril.*;
import api.simplelib.utils.ArrayUtils;
import com.google.gson.*;
import net.minecraft.nbt.NBTTagCompound;

import java.lang.reflect.Type;

/**
 * @author ci010
 */
public class CacheManifest
{
	public static final ITagSerializer<CacheManifest> nbtSerial = new ITagSerializer<CacheManifest>()
	{
		@Override
		public CacheManifest deserialize(NBTTagCompound tag)
		{
			String name = tag.getString("name");
			NBTListBuilder<CacheInfo> infos = NBTListBuilder.create(tag.getTagList("infos", NBTBases.COMPOUND), CacheInfo.nbtSeril);
			return new CacheManifest(name, infos.toArray(new CacheInfo[infos.size()]));
		}

		@Override
		public NBTTagCompound serialize(CacheManifest data)
		{
			return NBTTagBuilder.create().addString("name", data.name)
					.addTag("infos", NBTListBuilder.create(CacheInfo.class).appendAll(data.getInfo()).build())
					.addStringOption("version", data.version)
					.addStringOption("life", data.life)
					.build();
		}
	};
	public static final IJsonSerializer<CacheManifest> jsonSerial = new IJsonSerializer<CacheManifest>()
	{
		@Override
		public CacheManifest deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject object = json.getAsJsonObject();

			JsonArray array = object.getAsJsonArray("infos");
			CacheInfo[] cacheInfos = new CacheInfo[array.size()];
			for (int i = 0; i < array.size(); i++)
				cacheInfos[i] = context.deserialize(array.get(i), CacheInfo.class);
//			CacheInfo.jsonSeril.deserialize(array.get(i), typeOfT, context);
			CacheManifest manifest = new CacheManifest(object.get("name").getAsString(), cacheInfos);

			JsonElement tpElement = object.get("life"), vsElement = object.get("version");
			if (tpElement != null)
				manifest.life = tpElement.getAsString();
			if (vsElement != null)
				manifest.version = vsElement.getAsString();
			return manifest;
		}

		@Override
		public JsonElement serialize(CacheManifest src, Type typeOfSrc, JsonSerializationContext context)
		{
			JsonObject object = new JsonObject();

			object.addProperty("name", src.name);
			if (src.version != null)
				object.addProperty("version", src.version);
			if (src.life != null)
				object.addProperty("life", src.life);
			JsonArray arr = new JsonArray();
			for (CacheInfo cacheInfo : src.info)
				context.serialize(cacheInfo, CacheInfo.class);
			object.add("infos", arr);
			return object;
		}
	};
	private String name;
	private CacheInfo[] info;
	private String life, version;

	public CacheManifest(String name, CacheInfo[] info)
	{
		this.name = name;
		this.info = info;
	}

	public CacheManifest(String name, CacheInfo[] info, String type, String version)
	{
		this.name = name;
		this.info = info;
		this.life = type;
		this.version = version;
	}

	public String name()
	{
		return name;
	}

	public String getLife()
	{
		return life;
	}

	public String getVersion()
	{
		return version;
	}

	public CacheInfo[] getInfo()
	{
		return ArrayUtils.newArray(info);
	}
}
