package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public interface NBTDeserializer<T>
{
	T deserialize(NBTTagCompound tag);

	interface Base<T>
	{
		T deserialize(NBTBase base);
	}
}
