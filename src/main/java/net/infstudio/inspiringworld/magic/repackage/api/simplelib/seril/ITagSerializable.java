package net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author ci010
 */
public interface ITagSerializable
{
	void readFromNBT(NBTTagCompound tag);

	void writeToNBT(NBTTagCompound tag);

	interface Delegate<T>
	{
		void readFromNBT(T data, NBTTagCompound tag);

		void writeToNBT(T data, NBTTagCompound tag);
	}
}
