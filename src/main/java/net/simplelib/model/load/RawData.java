package net.simplelib.model.load;

import api.simplelib.seril.IJsonSerializer;
import com.google.common.collect.Maps;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * @author ci010
 */
public class RawData
{
	public String domain;
	public Map<String, RawModelData> models = Maps.newHashMap();
	public Map<String, RawAnimationData> animations = Maps.newHashMap();
	public Map<String, Operation> operations = Maps.newHashMap();

	class RawModelData
	{
		String parent;
		String group;
		int[] bound, rotation;
	}

	class Joint
	{
		String target;
		String face;
		int[] pos;
	}

	class RawAnimationData
	{
		String target;
		Operation[] operations;
		float time;
	}

	class Operation
	{
		String parent;
		int[] target;
		float start;
		float end;
		String type;
	}


	public static IJsonSerializer<RawData> newSeiralizer()
	{
		return new IJsonSerializer<RawData>()
		{
			@Override
			public JsonElement serialize(RawData src, Type typeOfSrc, JsonSerializationContext context)
			{
				return null;
			}

			@Override
			public RawData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
			{
				RawData rawModelData = new RawData();

				JsonObject asJsonObject = json.getAsJsonObject();
				JsonObject models = asJsonObject.get("models").getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : models.entrySet())
					rawModelData.models.put(entry.getKey(), context.<RawModelData>deserialize(entry.getValue(), RawModelData.class));

				JsonObject operations = asJsonObject.get("operations").getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : operations.entrySet())
					rawModelData.operations.put(entry.getKey(), context.<Operation>deserialize(entry.getValue(), Operation.class));

				JsonObject animations = asJsonObject.get("animations").getAsJsonObject();
				for (Map.Entry<String, JsonElement> entry : animations.entrySet())
					rawModelData.animations.put(entry.getKey(), context.<RawAnimationData>deserialize(entry.getValue(), RawAnimationData.class));
				return rawModelData;
			}
		};
	}

	public static RawData loadJson(String domain, String json)
	{
		RawData rawModelData = new GsonBuilder().registerTypeAdapter(RawData.class, newSeiralizer()).create()
			.fromJson(json, RawData.class);
		rawModelData.domain = domain;
		return rawModelData;
	}
}
