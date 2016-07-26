package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import gnu.trove.list.array.TIntArrayList;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagIntArray;

/**
 * @author ci010
 */
public class NBTArrayBuilder
{
	private TIntArrayList integers = new TIntArrayList();

	public NBTArrayBuilder add(int i)
	{
		integers.add(i);
		return this;
	}

	public NBTArrayBuilder add(int[] arr)
	{
		integers.add(arr);
		return this;
	}

	public NBTArrayBuilder add(byte[] arr)
	{
		int[] ints = new int[arr.length];
		for (int i = 0; i < ints.length; i++)
			ints[i] = arr[i];
		integers.add(ints);
		return this;
	}

	public static NBTArrayBuilder create()
	{
		return new NBTArrayBuilder();
	}

	private NBTArrayBuilder() {}

	public NBTTagIntArray build()
	{
		return new NBTTagIntArray(integers.toArray());
	}

	public NBTTagByteArray buildAsByte()
	{
		byte[] bytes = new byte[integers.size()];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) integers.get(i);
		return new NBTTagByteArray(bytes);
	}
}
