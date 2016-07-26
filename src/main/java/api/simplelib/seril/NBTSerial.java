package api.simplelib.seril;

import api.simplelib.utils.TypeUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import net.minecraft.nbt.*;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author ci010
 */
public class NBTSerial
{
	public static ITagSerializer.Base<BlockPos> posITagSerializer = new ITagSerializer.Base<BlockPos>()
	{
		@Override
		public BlockPos deserialize(NBTBase tag)
		{
			if (tag instanceof NBTTagIntArray)
			{
				int[] arr = ((NBTTagIntArray) tag).getIntArray();
				return new BlockPos(arr[0], arr[1], arr[2]);
			}
			return null;
		}

		@Override
		public NBTBase serialize(BlockPos data)
		{
			return NBTArrayBuilder.create().add(data.getX()).add(data.getY()).add(data.getZ()).build();
		}
	};
	private Map<Class, NBTDeserializer> deserializerMap;
	private Map<Class, NBTSerializer> serializerMap;

	public <T> NBTTagCompound toTag(T obj)
	{
		NBTBase base = this.toNBTBase(obj);
		NBTTagBuilder builder = NBTTagBuilder.create();
		if (base instanceof NBTTagCompound)
			return (NBTTagCompound) base;
		else
			return builder.addTag("obj", base).build();
	}

	public <T> Optional<T> fromTag(NBTTagCompound tag, Class<T> type)
	{
		if (deserializerMap.containsKey(type))
			return Optional.fromNullable(TypeUtils.<NBTDeserializer<T>>cast(deserializerMap.get(type)).deserialize(tag));
		return Optional.absent();
	}

	public NBTTagList toNBTList(List list)
	{
		NBTTagList nbtTagList = new NBTTagList();
		for (Object o : list)
			nbtTagList.appendTag(toNBTBase(o));
		return nbtTagList;
	}

	public List fromNBTList(NBTTagList list)
	{
		return null;
	}

	public  <T> NBTBase toNBTBase(T obj)
	{
		Class type = obj.getClass();
		if (serializerMap.containsKey(obj.getClass()))
			return TypeUtils.<NBTSerializer<T>>cast(serializerMap.get(obj.getClass())).serialize(obj);
		NBTBase base = NBTBases.instance().serialize(obj);
		if (base != null)
			return base;
		else
		{
			NBTTagBuilder builder = NBTTagBuilder.create();
			for (Field field : type.getDeclaredFields())
				if (field.isAccessible())
					try
					{
						Object fieldObj = field.get(obj);
						if (fieldObj != null)
							builder.addTag(field.getName(), toNBTBase(obj));
					}
					catch (IllegalAccessException e)
					{
						e.printStackTrace();
					}
			return builder.build();
		}
	}


	public <T> NBTDeserializer<T> getDeserializer(Class<T> clz)
	{
		if (deserializerMap.containsKey(clz))
			return TypeUtils.cast(deserializerMap.get(clz));
		return null;
	}

	public <T> NBTSerializer<T> getSerializer(Class<T> clz)
	{
		if (serializerMap.containsKey(clz))
			return TypeUtils.cast(serializerMap.get(clz));
		return null;
	}

	public <T> NBTSerial withDeserial(NBTDeserializer<T> deserializer, Class<T> clz)
	{
		if (deserializerMap == null)
			deserializerMap = Maps.newHashMap();
		deserializerMap.put(clz, deserializer);
		return this;
	}

	public <T> NBTSerial withSerial(NBTSerializer<T> serializer, Class<T> clz)
	{
		if (this.serializerMap == null)
			this.serializerMap = Maps.newHashMap();
		serializerMap.put(clz, serializer);
		return this;
	}

	public <T> NBTSerial with(ITagSerializer<T> serializer, Class<T> clz)
	{
		withDeserial(serializer, clz);
		withSerial(serializer, clz);
		return this;
	}

	public NBTSerial()
	{
	}
}
