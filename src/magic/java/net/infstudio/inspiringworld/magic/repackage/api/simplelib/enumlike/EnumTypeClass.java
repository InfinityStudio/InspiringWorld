package net.infstudio.inspiringworld.magic.repackage.api.simplelib.enumlike;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import gnu.trove.map.hash.TObjectByteHashMap;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.ArrayUtils;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author ci010
 */
public final class EnumTypeClass<T extends EnumType<T>>
{
	private static Map<String, EnumTypeClass> registerMap = Maps.newConcurrentMap();

	public static <T extends EnumType<T>> EnumTypeClass<T> valueOf(Class<T> type, String className)
	{
		EnumTypeClass enumLikeClass = registerMap.get(className);
		if (enumLikeClass.getRealType() != type)
			throw new IllegalArgumentException("The type mismatch " + type + " with " + enumLikeClass.getRealType());
		return TypeUtils.cast(enumLikeClass);
	}

	public static <T extends EnumType<T>> EnumTypeClass<T> valueOf(Class<T> type)
	{
		return valueOf(type, type.getSimpleName());
	}

	public static <T extends EnumType<T>> EnumTypeClass<T> define(Class<T> type, String className)
	{
		EnumTypeClass<T> likeClass = new EnumTypeClass<T>(type, className);
		registerMap.put(className, likeClass);
		return likeClass;
	}

	public static <T extends EnumType<T>> EnumTypeClass<T> define(Class<T> type)
	{
		return define(type, type.getSimpleName());
	}

	private java.lang.Class<T> realClass;
	private ArrayList<EnumType<T>> instances;
	private TObjectByteHashMap<String> map;
	private String name;

	private EnumTypeClass(Class<T> realClass, String className)
	{
		this.realClass = realClass;
		instances = Lists.newArrayList();
		map = new TObjectByteHashMap<String>();
		this.name = className;
	}

	public String name()
	{
		return name;
	}

	public EnumType<T> valueOf(String name)
	{
		return instances.get(map.get(name));
	}

	public EnumType<T>[] values()
	{
		return instances.toArray(ArrayUtils.<EnumType<T>>newArrayWithCapacity(instances.size()));
	}

	public int size()
	{
		return instances.size();
	}

	public java.lang.Class<T> getRealType()
	{
		return realClass;
	}

	void grow(EnumType<T> instance)
	{
		if (map.contains(instance.name()))
			throw new IllegalArgumentException("Duplicated enum instance name!");
		map.put(instance.name(), (byte) instances.size());
		instances.add(instance);
	}
}
