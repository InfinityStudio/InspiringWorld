package api.simplelib.utils;

import api.simplelib.seril.NBTBases;
import net.minecraft.nbt.NBTTagList;

/**
 * @author ci010
 */
public class NBTTagListUtils
{
	public static String[] asString(NBTTagList list)
	{
		if (list.getTagType() == NBTBases.STRING)
		{
			String[] arr = new String[list.tagCount()];
			for (int i = 0; i < list.tagCount(); i++)
				arr[i] = list.getStringTagAt(i);
			return arr;
		}
		return ArrayUtils.emptyArray();
	}

}
