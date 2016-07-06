package api.simplelib.seril;

import api.simplelib.utils.TypeUtils;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;

/**
 * @author ci010
 */
public class NBTBases implements NBTDeserializer.Base<Object>, NBTSerializer.Base<Object>
{
	/**
	 * The type id of the {@link NBTBase}.
	 */
	public static final byte END = 0, BYTE = 1, SHORT = 2, INT = 3, LONG = 4, FLOAT = 5, DOUBLE = 6, BYTE_ARRAY = 7,
			STRING = 8, TAG_LIST = 9, COMPOUND = 10, INT_ARRAY = 11;

	public static NBTBases instance()
	{
		if (instance == null)
			instance = new NBTBases();
		return instance;
	}

	public JsonElement toJson(NBTBase base)
	{
		return jsonMap.get(base.getId()).toJson(base);
	}

	public <T> NBTSerializer.Base<T> getSerializer(Class<T> tClass)
	{
		return TypeUtils.cast(built_in.get(this.getTypeIndex(tClass)));
	}

	public <T> FullSerializer<T> getFullSerializer(Class<T> tClass)
	{
		return TypeUtils.cast(built_in.get(this.getTypeIndex(tClass)));
	}

	public <T> NBTDeserializer.Base<T> getDeserializer(Class<T> tClass)
	{
		return TypeUtils.cast(built_in.get(this.getTypeIndex(tClass)));
	}

	public ImmutableList<FullSerializer> getList()
	{
		return built_in;
	}

	/**
	 * Determine if a type could be serialized to a {@link NBTBase}.
	 *
	 * @param type The type
	 * @return If the class is the basic type of NBT.
	 */
	public boolean isBaseType(Class type)
	{
		return built_in_map.containsKey(type);
	}

	/**
	 * Get the type id of this type.
	 *
	 * @param c The type
	 * @return The type id.
	 */
	public int getTypeIndex(Class c)
	{
		return built_in.indexOf(built_in_map.get(c));
	}

	/**
	 * Get the actual type from type id.
	 *
	 * @param i The type id
	 * @return The real type.
	 */
	public Class<?> getType(int i)
	{
		return built_in_map.inverse().get(built_in.get(i));
	}

	@Override
	public Object deserialize(NBTBase base)
	{
		if (base == null)
			return null;
		if (base.getId() == 10)
			throw new IllegalArgumentException("Cannot directly serialize an NBTTagCompound!");
		if (base.getId() == 9)
			throw new IllegalArgumentException("Cannot directly serialize an NBTTagList!");
		return TypeUtils.<NBTDeserializer.Base<Object>>cast(built_in.get(base.getId())).deserialize(base);
	}

	public <T> T deserializeTo(NBTBase base)
	{
		return TypeUtils.cast(deserialize(base));
	}

	@Override
	public NBTBase serialize(Object data)
	{
		FullSerializer serializer = built_in_map.get(data.getClass());
		if (serializer != null)
			return TypeUtils.<NBTSerializer<Object>>cast(serializer).serialize(data);
		return null;
	}

	private static NBTBases instance;

	private interface FullSerializer<T> extends NBTDeserializer.Base<T>, NBTSerializer.Base<T> {}

	private interface NBTToJson
	{
		JsonElement toJson(NBTBase base);
	}

	private ImmutableList<FullSerializer> built_in;
	private ImmutableBiMap<Class, FullSerializer> built_in_map;
	private ImmutableMap<Byte, NBTToJson> jsonMap;


	private NBTBases()
	{
		ImmutableList.Builder<FullSerializer> listBuilder = ImmutableList.builder();
		ImmutableBiMap.Builder<Class, FullSerializer> mapBuilder = ImmutableBiMap.builder();
		listBuilder.add(new FullSerializer<Void>()
		{
			@Override
			public NBTBase serialize(Void data)
			{
				return null;
			}

			@Override
			public Void deserialize(NBTBase base)
			{
				return null;
			}
		}).add(new FullSerializer<Byte>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Byte data)
			{
				return new NBTTagByte(data);
			}

			@Override
			public Byte deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getByte();
			}
		}).add(new FullSerializer<Short>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Short data)
			{
				return new NBTTagShort(data);
			}

			@Override
			public Short deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getShort();
			}
		}).add(new FullSerializer<Integer>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Integer data)
			{
				return new NBTTagInt(data);
			}

			@Override
			public Integer deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getInt();
			}
		}).add(new FullSerializer<Long>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Long data)
			{
				return new NBTTagLong(data);
			}

			@Override
			public Long deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getLong();
			}
		}).add(new FullSerializer<Float>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Float data)
			{
				return new NBTTagFloat(data);
			}

			@Override
			public Float deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getFloat();
			}

		}).add(new FullSerializer<Double>()
		{
			@Override
			public NBTBase.NBTPrimitive serialize(Double data)
			{
				return new NBTTagDouble(data);
			}

			@Override
			public Double deserialize(NBTBase base)
			{
				return ((NBTBase.NBTPrimitive) base).getDouble();
			}
		}).add(new FullSerializer<byte[]>()
		{
			@Override
			public NBTTagByteArray serialize(byte[] data)
			{
				return new NBTTagByteArray(data);
			}

			@Override
			public byte[] deserialize(NBTBase base)
			{
				return ((NBTTagByteArray) base).getByteArray();
			}
		}).add(new FullSerializer<String>()
		{
			@Override
			public NBTTagString serialize(String data)
			{
				return new NBTTagString(data);
			}

			@Override
			public String deserialize(NBTBase base)
			{
				return ((NBTTagString) base).getString();
			}
		}).add(new FullSerializer<List>()
		{
			@Override
			public NBTBase serialize(List data)
			{
				HashSet<Class> set = new HashSet<Class>();
				for (Object o : data)
					set.add(o.getClass());
				if (set.size() == 1)
				{

				}
				else
				{
					new NBTTagList();
				}
				return null;
			}

			@Override
			public List deserialize(NBTBase base)
			{
				return null;
			}
		}).add(new FullSerializer<Object>()
		{
			@Override
			public Object deserialize(NBTBase base)
			{
				return null;
			}

			@Override
			public NBTTagCompound serialize(Object data)
			{
				NBTTagCompound tag = new NBTTagCompound();
				Class<?> clz = data.getClass();
				for (Field field : clz.getDeclaredFields())
				{
					try
					{
						Class type = field.getType();
						if (isBaseType(type))
						{
							NBTSerializer.Base base = NBTBases.this.getSerializer(type);
							if (!field.isAccessible())
								field.setAccessible(true);
							Object f = field.get(data);
							tag.setTag(field.getName(), base.serialize(f));
						}
						else if (field.isAnnotationPresent(Include.class))
						{

						}
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
				}
				return tag;
			}
		}).add(new FullSerializer<int[]>()
		{
			@Override
			public NBTTagIntArray serialize(int[] data)
			{
				return new NBTTagIntArray(data);
			}

			@Override
			public int[] deserialize(NBTBase base)
			{
				return ((NBTTagIntArray) base).getIntArray();
			}
		});
		built_in = listBuilder.build();
		for (FullSerializer fullSerializer : built_in)
			mapBuilder.put(TypeUtils.getInterfaceGenericTypeTo(fullSerializer), fullSerializer);
		built_in_map = mapBuilder.build();

		ImmutableMap.Builder<Byte, NBTToJson> jsonBuilder = ImmutableMap.builder();

		jsonBuilder.put(NBTBases.BYTE, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getByte());
			}
		});
		jsonBuilder.put(NBTBases.SHORT, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getShort());
			}
		});
		jsonBuilder.put(NBTBases.LONG, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getLong());
			}
		});
		jsonBuilder.put(NBTBases.INT, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getInt());
			}
		});
		jsonBuilder.put(NBTBases.FLOAT, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getFloat());
			}
		});
		jsonBuilder.put(NBTBases.DOUBLE, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTBase.NBTPrimitive.class.cast(base).getDouble());
			}
		});
		jsonBuilder.put(NBTBases.STRING, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				return new JsonPrimitive(NBTTagString.class.cast(base).getString());
			}
		});
		NBTToJson arrTo = new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				JsonArray arr = new JsonArray();
				if (base instanceof NBTTagList)
				{
					for (int i = 0; i < ((NBTTagList) base).tagCount(); i++)
					{
						NBTBase b = ((NBTTagList) base).get(i);
						arr.add(jsonMap.get(b.getId()).toJson(b));
					}
				}
				else if (base instanceof NBTTagIntArray)
				{
					for (int i : ((NBTTagIntArray) base).getIntArray())
						arr.add(new JsonPrimitive(i));
				}
				else if (base instanceof NBTTagByteArray)
				{
					for (byte b : ((NBTTagByteArray) base).getByteArray())
						arr.add(new JsonPrimitive(b));
				}
				return arr;
			}
		};
		jsonBuilder.put(NBTBases.TAG_LIST, arrTo);
		jsonBuilder.put(NBTBases.INT_ARRAY, arrTo);
		jsonBuilder.put(NBTBases.BYTE_ARRAY, arrTo);
		jsonBuilder.put(NBTBases.COMPOUND, new NBTToJson()
		{
			@Override
			public JsonElement toJson(NBTBase base)
			{
				JsonObject object = new JsonObject();
				NBTTagCompound compound = NBTTagCompound.class.cast(base);
				for (String s : compound.getKeySet())
				{
					NBTBase tag = compound.getTag(s);
					object.add(s, jsonMap.get(tag.getId()).toJson(tag));
				}
				return object;
			}
		});
		jsonMap = jsonBuilder.build();
	}

	interface JsonToNBT
	{
		NBTBase toNBT(JsonElement e);
	}

	{
		GsonBuilder gsonBuilder = new GsonBuilder();
		new JsonToNBT()
		{
			@Override
			public NBTBase toNBT(JsonElement e)
			{
				return new NBTTagString(e.getAsString());
			}
		};

		new JsonToNBT()
		{
			@Override
			public NBTBase toNBT(JsonElement e)
			{
				Number asNumber = e.getAsJsonPrimitive().getAsNumber();
				byte asByte = asNumber.byteValue();
				int asInt = asNumber.intValue();
				if (asByte == asInt)
					return new NBTTagByte(asByte);
				float asFloat = asNumber.floatValue();
				if (asFloat == asFloat)
					return new NBTTagInt(asInt);
				double asDouble = asNumber.doubleValue();
				if (asDouble == asFloat)
					return new NBTTagFloat(asFloat);
				return new NBTTagDouble(asDouble);
			}
		};

		new JsonToNBT()
		{
			@Override
			public NBTBase toNBT(JsonElement e)
			{
				JsonArray array = e.getAsJsonArray();
				for (JsonElement jsonElement : array)
				{
					jsonElement.isJsonObject();
				}
				return null;
			}
		};
	}
}
