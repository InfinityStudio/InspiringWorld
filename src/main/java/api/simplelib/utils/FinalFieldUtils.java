package api.simplelib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mickey
 * @author ci010
 */
public enum FinalFieldUtils
{
	INSTANCE;

	private Object unsafeObj;
	private Method
			putObjectMethod,
			objectFieldOffsetMethod,
			staticFieldOffsetMethod,
			staticFieldBaseMethod;

	FinalFieldUtils()
	{
		try
		{
			final Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");

			final Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");

			unsafeField.setAccessible(true);

			unsafeObj = unsafeField.get(null);

			putObjectMethod = unsafeClass.getMethod("putObject", Object.class, long.class, Object.class);

			objectFieldOffsetMethod = unsafeClass.getMethod("objectFieldOffset", Field.class);

			staticFieldOffsetMethod = unsafeClass.getMethod("staticFieldOffset", Field.class);

			staticFieldBaseMethod = unsafeClass.getMethod("staticFieldBase", Field.class);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchFieldException e)
		{
			e.printStackTrace();
		}

	}

	public long getFieldOffset(final Object o, final Field field) throws InvocationTargetException, IllegalAccessException
	{
		return (Long) objectFieldOffsetMethod.invoke(unsafeObj, field);
	}

	public void set(Object o, final long fieldOffset, final Object value) throws Exception
	{
		final Object fieldBase = o;
		putObjectMethod.invoke(unsafeObj, fieldBase, fieldOffset, value);
	}

	public void set(final Object o, final Field field, final Object value) throws Exception
	{
		final Object fieldBase = o;
		final long fieldOffset = (Long) objectFieldOffsetMethod.invoke(unsafeObj, field);

		putObjectMethod.invoke(unsafeObj, fieldBase, fieldOffset, value);
	}

	public void setStatic(final Field field, final Object value) throws Exception
	{
		final Object fieldBase = staticFieldBaseMethod.invoke(unsafeObj, field);
		final long fieldOffset = (Long) staticFieldOffsetMethod.invoke(unsafeObj, field);

		putObjectMethod.invoke(unsafeObj, fieldBase, fieldOffset, value);
	}
}
