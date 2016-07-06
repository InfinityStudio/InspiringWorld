package api.simplelib.seril;

import api.simplelib.utils.TypeUtils;
import com.google.common.collect.Maps;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;
import java.util.Map;

/**
 * @author ci010
 */
public class DataSerializerView
{
	private static Map<Class, DataSerializer> serializerMap = Maps.newHashMap();

	public static abstract class DataSerializerBase<T> implements DataSerializer<T>
	{
		@Override
		public DataParameter<T> createKey(int id)
		{
			return new DataParameter<T>(id, this);
		}
	}

	public static final DataSerializer<Short> SHORT = new DataSerializerBase<Short>()
	{
		@Override
		public void write(PacketBuffer buf, Short value) {buf.writeShort(value);}

		@Override
		public Short read(PacketBuffer buf) throws IOException {return buf.readShort();}
	};
	public static final DataSerializer<Long> LONG = new DataSerializerBase<Long>()
	{
		@Override
		public void write(PacketBuffer buf, Long value) {buf.writeLong(value);}

		@Override
		public Long read(PacketBuffer buf) throws IOException {return buf.readLong();}
	};

	public static final DataSerializer<Double> DOUBLE = new DataSerializerBase<Double>()
	{
		@Override
		public void write(PacketBuffer buf, Double value) {buf.writeDouble(value);}

		@Override
		public Double read(PacketBuffer buf) throws IOException {return buf.readDouble();}
	};

	static
	{
		DataSerializers.registerSerializer(SHORT);
		DataSerializers.registerSerializer(LONG);
		DataSerializers.registerSerializer(DOUBLE);
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			DataSerializer<?> serializer = DataSerializers.getSerializer(i);
			if (serializer == null) break;
			serializerMap.put(TypeUtils.getInterfaceGenericTypeTo(serializer.getClass(), DataSerializer.class), serializer);
		}
	}

	public static <T> DataSerializer<T> getByClass(final Class<T> type)
	{
		return serializerMap.get(type);
	}

	public static <T extends Enum<T>> DataSerializer<T> createForEnum(Class<T> type)
	{
		return new EnumSerial<T>(type);
	}

	public static class EnumSerial<T extends Enum<T>> extends DataSerializerBase<T>
	{
		private Class<T> tClass;

		public EnumSerial(Class<T> tClass)
		{
			this.tClass = tClass;
		}

		@Override
		public void write(PacketBuffer buf, T value)
		{
			buf.writeEnumValue(value);
		}

		@Override
		public T read(PacketBuffer buf) throws IOException
		{
			return buf.readEnumValue(tClass);
		}
	}
}
