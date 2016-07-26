package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import com.google.common.collect.Lists;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.TypeUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

/**
 * @author ci010
 */
public class NBTListBuilder<T> implements List<T>
{
	private List<T> list;
	private Class<T> type;
	private int tagType;
	private ITagSerializer<T> serializer;
	private boolean serializable = false;

	/**
	 * Create a new list builder from a specific type.
	 *
	 * @param type The list's generic type class.
	 * @param <T>  The list's generic type.
	 * @return A new list.
	 */
	public static <T> NBTListBuilder<T> create(Class<T> type)
	{
		if (!NBTBases.instance().isBaseType(type) && !ITagSerializable.class.isAssignableFrom(type))
			throw new IllegalArgumentException("Not support " + type + " for nbt list");
		return new NBTListBuilder<T>(type);
	}

	public static <T> NBTListBuilder<T> create(Class<T> type, ITagSerializer<T> serializer)
	{
		return new NBTListBuilder<T>(type).setSerial(serializer);
	}

	/**
	 * Create a new list builder from a existed {@link NBTTagList}.
	 * <p>This will put the parameter list as the reference. of this builder. And won't create a new list.</p>
	 *
	 * @param list The list already exist.
	 * @param <T>  The list's generic type.
	 * @return A new list.
	 */

	public static <T> NBTListBuilder<T> create(NBTTagList list)
	{
		int idx = list.getTagType();
		if (idx == 0)
			throw new IllegalArgumentException("The list should determine a specific type! At least add an element to" +
					" it.");
		return new NBTListBuilder<T>(TypeUtils.<T>cast(NBTBases.instance().getType(idx)), list);
	}

	public static <T> NBTListBuilder<T> create(NBTTagList list, ITagSerializer<T> serializer)
	{
		NBTListBuilder<T> builder = create(list);
		return builder.setSerial(serializer);
	}

	/**
	 * Create a new list builder from a existed {@link NBTTagList}.
	 * <p>This will copy the old list to the new list with copied new data.</p>
	 *
	 * @param list The list already exist.
	 * @param <T>  The list's generic type.
	 * @return A new list.
	 */
	public static <T> NBTListBuilder<T> copyOf(NBTTagList list)
	{
		return create((NBTTagList) list.copy());
	}

	NBTListBuilder<T> setSerial(ITagSerializer<T> serial)
	{
		this.serializer = serial;
		return this;
	}

	private NBTListBuilder(Class<T> type)
	{
		list = new ArrayList<T>();
		serializable = ITagSerializable.class.isAssignableFrom(type);
		if (serializable)
			tagType = NBTBases.COMPOUND;
		else this.tagType = NBTBases.instance().getTypeIndex(type);

		this.type = type;
	}

	private NBTListBuilder(Class<T> type, NBTTagList list)
	{
		this.type = type;
		this.list = new ArrayList<T>(list.tagCount());
		this.tagType = NBTBases.instance().getTypeIndex(type);
		for (int i = 0; i < list.tagCount(); i++)
			this.list.add(NBTBases.instance().<T>deserializeTo(list.get(i)));
	}

	public NBTListBuilder<T> append(T element)
	{
		this.add(element);
		return this;
	}

	public NBTListBuilder<T> appendAll(T... element)
	{
		this.addAll(Lists.newArrayList(element));
		return this;
	}

	public NBTListBuilder<T> appendAll(Collection<T> element)
	{
		this.addAll(element);
		return this;
	}

	@Override
	public T set(int index, T element) {return list.set(index, element);}

	@Override
	public void add(int index, T element) {list.add(index, element);}

	@Override
	public T remove(int index) {return list.remove(index);}

	@Override
	public int indexOf(Object o) {return list.indexOf(o);}

	@Override
	public int lastIndexOf(Object o) {return list.lastIndexOf(o);}

	@Override
	public ListIterator<T> listIterator() {return list.listIterator();}

	@Override
	public ListIterator<T> listIterator(int index) {return list.listIterator(index);}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {return list.subList(fromIndex, toIndex);}

	@Override
	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {return list.contains(o);}

	@Override
	public int size()
	{
		return list.size();
	}

	public Class<T> getTagRealType()
	{
		return type;
	}

	@Override
	public Iterator<T> iterator()
	{
		return list.iterator();
	}

	@Override
	public Object[] toArray() {return list.toArray();}

	@Override
	public <T1> T1[] toArray(T1[] a) {return list.toArray(a);}

	@Override
	public boolean add(T t) {return list.add(t);}

	@Override
	public boolean remove(Object o) {return list.remove(o);}

	@Override
	public boolean containsAll(Collection<?> c) {return list.containsAll(c);}

	@Override
	public boolean addAll(Collection<? extends T> c) {return list.addAll(c);}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {return list.addAll(index, c);}

	@Override
	public boolean removeAll(Collection<?> c) {return list.removeAll(c);}

	@Override
	public boolean retainAll(Collection<?> c) {return list.retainAll(c);}

	@Override
	public void clear() {list.clear();}

	@Override
	public boolean equals(Object o)
	{
//		if (o instanceof NBTTagList)
//		{
//			NBTTagList lst = (NBTTagList) o;
//			if (lst.getTagType() == this.tagType)
//			{
//				for (int i = 0; i < lst.tagCount(); i++)
//				{
//					lst.get(i).equals()
//				}
//			}
//		}
		//TODO consider this....
		return list.equals(o);
	}

	@Override
	public int hashCode() {return list.hashCode();}

	@Override
	public T get(int index)
	{
		return list.get(index);
	}

	public NBTTagList build()
	{
		NBTTagList lst = new NBTTagList();
		for (T t : this.list)
			if (serializer != null)
				lst.appendTag(serializer.serialize(t));
			else if (serializable)
			{
				ITagSerializable serializable = (ITagSerializable) t;
				NBTTagCompound compound = new NBTTagCompound();
				serializable.writeToNBT(compound);
				lst.appendTag(compound);
			}
			else
				lst.appendTag(NBTBases.instance().serialize(t));
		return lst;
	}
}
