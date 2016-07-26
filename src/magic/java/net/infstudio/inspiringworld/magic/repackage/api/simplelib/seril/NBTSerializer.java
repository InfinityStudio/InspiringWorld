package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public interface NBTSerializer<T>
{
	NBTTagCompound serialize(T data);

	interface Base<T>
	{
		NBTBase serialize(T data);
	}
}
