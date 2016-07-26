package api.simplelib.seril;

import api.simplelib.utils.ArrayUtils;
import com.google.common.base.Function;
import com.google.gson.*;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.lang.reflect.*;
import java.util.Map;
import java.util.Set;

/**
 * @author ci010
 */
public class ReflectTagSerializer<T> implements ITagSerializer<T>
{
	Constructor<T> constructor;
	Function<NBTTagCompound, Object>[] functions;
	SerialBean[] bean;
	NBTSerial parent;

	@Override
	public T deserialize(NBTTagCompound tag)
	{
		Object[] args = new Object[functions.length];
		for (int i = 0; i < args.length; i++)
			args[i] = functions[i].apply(tag);
		try
		{
			return constructor.newInstance(args);
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public NBTTagCompound serialize(T data)
	{
		NBTTagCompound tag = new NBTTagCompound();
		for (SerialBean nbtBean : bean)
			tag.setTag(nbtBean.name, parent.toNBTBase(nbtBean.apply(data)));
		return tag;
	}

	JsonDeserializer<ReflectTagSerializer> deserializer = new JsonDeserializer<ReflectTagSerializer>()
	{
		SerialBean[] handleSer(Class target, JsonObject serialize)
		{
			Set<Map.Entry<String, JsonElement>> entries = serialize.entrySet();
			SerialBean[] beans = ArrayUtils.newArrayWithCapacity(entries.size());
			int i = 0;
			for (Map.Entry<String, JsonElement> entry : entries)
			{
				final String name = entry.getKey();
				final String op = entry.getValue().getAsJsonPrimitive().getAsString();
				SerialBean bean = null;
				if (op.contains("()"))
					try {bean = new MethodBean(name, target.getMethod(op));}
					catch (NoSuchMethodException e) {e.printStackTrace();}
				else
					try {bean = new FieldBean(name, target.getDeclaredField(op));}
					catch (NoSuchFieldException e)
					{
						try {bean = new FieldBean(name, target.getField(op));}
						catch (NoSuchFieldException e1) {e.printStackTrace(); e1.printStackTrace();}
					}
				if (bean == null) throw new IllegalArgumentException();
				beans[i++] = bean;
			}
			return beans;
		}

		@Override
		public ReflectTagSerializer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
		{
			JsonObject obj = json.getAsJsonObject();
			JsonObject serialize = obj.get("serialize").getAsJsonObject();
			JsonObject deserialize = obj.get("deserialize").getAsJsonObject();
			Class target = null;
			try
			{
				target = Class.forName(obj.get("target").getAsString());
			}
			catch (ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			if (target == null)
				return null;
			SerialBean[] objectBeen = handleSer(target, serialize);
			handleDeSer(target, deserialize);
			return null;
		}

		private Function<NBTTagCompound, Object>[] handleDeSer(Class target, JsonObject deserialize)
		{
			return null;
		}
	};

	private abstract class DeSerialBean implements Function<NBTTagCompound, Object>
	{

	}

	class ConstructorBean extends DeSerialBean
	{
		Constructor constructor;
		String[] argsGet;

		@Nullable
		@Override
		public Object apply(@Nullable NBTTagCompound input)
		{
			return null;
//			constructor
		}
	}

	private abstract class SerialBean implements Function<Object, Object>
	{
		String name;

		public SerialBean(String name) {this.name = name;}
	}

	class FieldBean extends SerialBean
	{
		Field field;

		public FieldBean(String name, Field field)
		{
			super(name);
			this.field = field;
		}

		@Nullable
		@Override
		public Object apply(@Nullable Object input)
		{
			if (!field.isAccessible())
				field.setAccessible(true);
			try
			{
				return field.get(input);
			}
			catch (IllegalAccessException ignored) {}
			return null;
		}
	}

	class MethodBean extends SerialBean
	{
		Method method;

		public MethodBean(String name, Method method)
		{
			super(name);
			this.method = method;
		}

		public Object apply(@Nullable Object input)
		{
			try
			{
				return method.invoke(input);
			}
			catch (IllegalAccessException e)
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
			return null;
		}
	}


}
