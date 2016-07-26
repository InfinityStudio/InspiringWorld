package net.infstudio.inspiringworld.magic.repackage.net.simplelib.recipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * @author ci010
 */
public class ItemStacks implements Comparable<ItemStacks>
{
	protected String name;
	protected ItemStack[] materials;
	protected int size, length, height;
	protected boolean damageSensitive, nbtSensitive, raw, oreDic;

	public ItemStacks(String name, int length, int height, ItemStack... materials)
	{
		this.name = name;
		this.materials = materials;
		this.length = length;
		this.height = height;
		this.size = length * height;
		if (this.size != materials.length)
			throw new IllegalArgumentException("Stack size error.");
	}

	public ItemStacks(ItemStack... materials)
	{
		this.materials = materials;
		this.size = materials.length;
		this.raw = true;
	}

	public ItemStacks damageSensitive()
	{
		this.damageSensitive = true;
		return this;
	}

	public ItemStacks nbtSensitive()
	{
		this.nbtSensitive = true;
		return this;
	}

	public int size()
	{
		return size;
	}

	@Override
	public int compareTo(ItemStacks o)
	{
		if (o == null)
			return 1;
		if (!o.raw && !this.raw)
			return o.name.equals(this.name) ? 0 : 1;
		if (o.size() != this.size())
			return o.size() - this.size();
		if (o.length != this.length)
			return o.length - this.length;
		if (o.height != this.height)
			return o.height - this.height;
		ItemStack stack, other;
		for (int i = 0; i < this.size(); ++i)
		{
			stack = o.materials[i];
			other = this.materials[i];
			if (stack == null && other == null)
				continue;
			if (other == null || stack == null)
				return -1;
			if (this.nbtSensitive || o.nbtSensitive)
				if (!stack.getTagCompound().equals(other.getTagCompound()))
					return -1;
			int thisIds[] = OreDictionary.getOreIDs(stack), otherIds[] = OreDictionary.getOreIDs(other);
			for (int j = 0; j < thisIds.length; ++j)
				if (thisIds[j] != otherIds[j])
					return -1;
			if (other.getItem() != stack.getItem() ||
					((damageSensitive || o.damageSensitive) && other.getItemDamage() != stack.getItemDamage()))
				return -1;
		}
		return 0;
	}
}
