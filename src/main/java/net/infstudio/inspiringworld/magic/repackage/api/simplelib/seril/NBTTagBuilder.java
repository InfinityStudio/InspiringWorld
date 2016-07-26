package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import joptsimple.internal.Strings;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public class NBTTagBuilder
{
	private NBTTagCompound tag;

	private NBTTagBuilder()
	{
		this(new NBTTagCompound());
	}

	private NBTTagBuilder(NBTTagCompound tagCompound)
	{
		this.tag = tagCompound;
	}

	public static NBTTagBuilder create()
	{
		return new NBTTagBuilder();
	}

	public static NBTTagBuilder create(NBTTagCompound tag)
	{
		return new NBTTagBuilder(tag);
	}

	public NBTTagBuilder addInt(String key, int data)
	{
		tag.setInteger(key, data);
		return this;
	}

	public NBTTagBuilder addShort(String key, short data)
	{
		tag.setShort(key, data);
		return this;
	}

	public NBTTagBuilder addLong(String key, long data)
	{
		tag.setLong(key, data);
		return this;
	}

	public NBTTagBuilder addFloat(String key, float data)
	{
		tag.setFloat(key, data);
		return this;
	}

	public NBTTagBuilder addDouble(String key, double data)
	{
		tag.setDouble(key, data);
		return this;
	}

	public NBTTagBuilder addByte(String key, byte data)
	{
		tag.setByte(key, data);
		return this;
	}

	public NBTTagBuilder addBoolean(String key, boolean data)
	{
		tag.setBoolean(key, data);
		return this;
	}

	public NBTTagBuilder addString(String key, String data)
	{
		tag.setString(key, data);
		return this;
	}

	public NBTTagBuilder addStringOption(String key, String data)
	{
		if (data == null || data.equals(Strings.EMPTY))
			return this;
		tag.setString(key, data);
		return this;
	}

	public NBTTagBuilder addByteArray(String key, byte[] data)
	{
		tag.setByteArray(key, data);
		return this;
	}

	public NBTTagBuilder addIntArray(String key, int[] data)
	{
		tag.setIntArray(key, data);
		return this;
	}

	public NBTTagBuilder addTag(String key, NBTBase data)
	{
		tag.setTag(key, data);
		return this;
	}

	public NBTTagBuilder copyFrom(NBTTagCompound tagCompound)
	{
		for (String s : tagCompound.getKeySet())
			this.addTag(s, tagCompound.getTag(s));
		return this;
	}

	public NBTTagBuilder copyTo(NBTTagCompound tagCompound)
	{
		for (String s : this.tag.getKeySet())
			tagCompound.setTag(s, this.tag.getTag(s));
		return this;
	}

	public NBTTagCompound build()
	{
		return this.tag;
	}
}
