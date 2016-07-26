package net.simplelib.inventory;

import api.simplelib.inventory.Inventory;
import api.simplelib.inventory.InventoryRule;
import api.simplelib.inventory.InventorySlot;
import com.google.common.base.Optional;
import com.google.common.collect.Iterators;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import net.minecraft.item.ItemStack;

import java.util.Iterator;

/**
 * @author ci010
 */
public class SlotSpaceImpl implements InventorySlot
{
	private InventoryRule rule;
	private String name;
	private Inventory parent;
	private int index;

	public SlotSpaceImpl(String name, Inventory parent, int index, InventoryRule rule)
	{
		this.name = name;
		this.parent = parent;
		this.index = index;
		this.rule = rule == null ? InventoryRule.COMMON : rule;
	}

	void setName(String name)
	{
		this.name = name;
	}

	public InventoryRule getRule()
	{
		return rule;
	}

	@Override
	public Inventory parent()
	{
		return parent;
	}

	@Override
	public String name()
	{
		return name;
	}

	@Override
	public int id()
	{
		return index;
	}

	public void setRule(InventoryRule rule)
	{
		this.rule = rule;
	}

	@Override
	public ItemStack getStackInSlot()
	{
		return parent.asItemHandler().getStackInSlot(index);
	}

	@Override
	public ItemStack insertItem(ItemStack stack, boolean simulate)
	{
		return parent.asItemHandler().insertItem(index, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int amount, boolean simulate)
	{
		return parent.asItemHandler().extractItem(index, amount, simulate);
	}

	@Override
	public Iterator<ItemStack> iterator()
	{
		return Iterators.forArray(getStackInSlot());
	}
}
