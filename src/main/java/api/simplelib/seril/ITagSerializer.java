package api.simplelib.seril;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * @author ci010
 */
public interface ITagSerializer<T> extends NBTSerializer<T>, NBTDeserializer<T>
{
	class Builder
	{
		public static <T> ITagSerializer<T> newSerializer(final NBTSerializer<T> serializer, final NBTDeserializer<T> deserializer)
		{
			return new ITagSerializer<T>()
			{
				@Override
				public T deserialize(NBTTagCompound tag)
				{
					return deserializer.deserialize(tag);
				}

				@Override
				public NBTTagCompound serialize(T data)
				{
					return serializer.serialize(data);
				}
			};
		}

		public static <T> ITagSerializer<T> newSerializer(final String name,
														  final NBTSerializer.Base<T> serializer,
														  final NBTDeserializer.Base<T>
																  deserializer)
		{
			return new ITagSerializer<T>()
			{
				@Override
				public T deserialize(NBTTagCompound tag)
				{
					return deserializer.deserialize(tag.getTag(name));
				}

				@Override
				public NBTTagCompound serialize(T data)
				{
					return NBTTagBuilder.create().addTag(name, serializer.serialize(data)).build();
				}
			};
		}

		public static <T> ITagSerializer<T> newTempSerializer(final NBTSerializer.Base<T> serializer,
															  final NBTDeserializer.Base<T>
																	  deserializer)
		{
			return new ITagSerializer<T>()
			{
				String id = UUID.randomUUID().toString();

				@Override
				public T deserialize(NBTTagCompound tag)
				{
					return deserializer.deserialize(tag.getTag(id));
				}

				@Override
				public NBTTagCompound serialize(T data)
				{
					return NBTTagBuilder.create().addTag(id, serializer.serialize(data)).build();
				}
			};
		}
	}

	interface Base<T> extends NBTSerializer.Base<T>, NBTDeserializer.Base<T>
	{}
}
