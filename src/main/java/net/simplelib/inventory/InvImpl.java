package net.simplelib.inventory;

import api.simplelib.inventory.Inventory;
import api.simplelib.inventory.InventoryElement;
import api.simplelib.inventory.InventorySlot;
import api.simplelib.inventory.InventorySpace;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import javafx.beans.InvalidationListener;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;

/**
 * @author ci010
 */
public class InvImpl implements Inventory, ISidedInventory, IItemHandlerModifiable
{
	private ItemStack[] stacks;
	private int size;

	private EnumMap<EnumFacing, int[]> sideMap;

	private InventoryElement[] elements;
	private ImmutableMap<String, InventoryElement> elementMap;

	private List<InvalidationListener> listeners = Lists.newArrayList();
	private IItemHandler wrap = new InvWrapper(this)
	{
		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (stack == null)
				return null;

			if (!InvImpl.this.isItemValidForSlot(slot, stack))
				return stack;

			ItemStack stackInSlot = InvImpl.this.getStackInSlot(slot);

			int m;
			if (stackInSlot != null)
			{
				if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot))
					return stack;

				m = Math.min(stack.getMaxStackSize(), elements[slot].getRule().getInventoryStackLimit()) - stackInSlot.stackSize;

				if (stack.stackSize <= m)
				{
					if (!simulate)
					{
						ItemStack copy = stack.copy();
						copy.stackSize += stackInSlot.stackSize;
						InvImpl.this.setInventorySlotContents(slot, copy);
						InvImpl.this.markDirty();
					}
					return null;
				}
				else
				{
					if (!simulate)
					{
						ItemStack copy = stack.splitStack(m);
						copy.stackSize += stackInSlot.stackSize;
						InvImpl.this.setInventorySlotContents(slot, copy);
						InvImpl.this.markDirty();
						return stack;
					}
					else
					{
						stack.stackSize -= m;
						return stack;
					}
				}
			}
			else
			{
				m = Math.min(stack.getMaxStackSize(), elements[slot].getRule().getInventoryStackLimit());
				if (m < stack.stackSize)
				{
					if (!simulate)
					{
						InvImpl.this.setInventorySlotContents(slot, stack.splitStack(m));
						InvImpl.this.markDirty();
						return stack;
					}
					else
					{
						stack.stackSize -= m;
						return stack;
					}
				}
				else
				{
					if (!simulate)
					{
						InvImpl.this.setInventorySlotContents(slot, stack);
						InvImpl.this.markDirty();
					}
					return null;
				}
			}
		}
	};

	public InvImpl()
	{
		this.wrap = new InvWrapper(this);
	}

	void build(EnumMap<EnumFacing, int[]> sideMap, int size, ArrayList<InventoryElement> elements)
	{
		this.sideMap = sideMap.clone();
		this.size = size;
		this.elements = new InventoryElement[size];
		ImmutableMap.Builder<String, InventoryElement> builder = ImmutableMap.builder();
		for (InventoryElement element : elements)
		{
			if (element instanceof InventorySlot)
				this.elements[element.id()] = element;
			else if (element instanceof InventorySpace)
				for (int j = 0; j < ((InventorySpace) element).getSlots(); j++)
					this.elements[element.id() + j] = element;
			builder.put(element.name(), element);
		}
		this.stacks = new ItemStack[this.size];
		this.elementMap = builder.build();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return sideMap != null ? sideMap.get(side).clone() : new int[0];
	}

	@Override
	public int getSizeInventory()
	{
		return size;
	}

	@Override
	public int getSlots()
	{
		return wrap.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return stacks[index];
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
	{
		return wrap.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate)
	{
		return wrap.extractItem(slot, amount, simulate);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		if (this.stacks[index] != null)
		{
			ItemStack stack;
			if (this.stacks[index].stackSize <= size)
			{
				stack = this.stacks[index];
				this.stacks[index] = null;
				this.markDirty();
				return stack;
			}
			else
			{
				stack = this.stacks[index].splitStack(size);
				if (this.stacks[index].stackSize == 0)
					this.stacks[index] = null;
				this.markDirty();
				return stack;
			}
		}
		else
			return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		if (this.stacks[index] != null)
		{
			ItemStack itemstack = this.stacks[index];
			this.stacks[index] = null;
			return itemstack;
		}
		else
			return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.stacks[index] = stack;
		if (stack != null && stack.stackSize > this.getInventoryStackLimit())
			stack.stackSize = this.getInventoryStackLimit();
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {return 64;}

	@Override
	public void markDirty()
	{
		for (InvalidationListener listener : listeners)
			listener.invalidated(this);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return elements[index].getRule().isItemValid(stack);
	}

	@Override
	public int getField(int id) {return 0;}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {return 0;}

	@Override
	public void clear()
	{
		for (int i = 0; i < this.stacks.length; i++)
			stacks[i] = null;
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		if (sideMap.containsKey(direction))
			for (int i : sideMap.get(direction))
				if (index == i)
					return this.elements[index].getRule().isItemValid(itemStackIn);
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		if (sideMap.containsKey(direction))
			for (int i : sideMap.get(direction))
				if (index == i)
					return this.elements[index].getRule().isItemValid(stack);
		return false;
	}

	@Override
	public String getName()
	{
		return "Inventory_Impl";
	}

	@Override
	public boolean hasCustomName()
	{
		return false;
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TextComponentString(this.getName());
	}

	@Override
	public void addListener(InvalidationListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public InventoryElement getByName(String name)
	{
		return elementMap.get(name);
	}

	@Override
	public Collection<InventoryElement> getAll()
	{
		return elementMap.values();
	}

	@Override
	public InventoryElement getById(int id)
	{
		return elements[id];
	}

	@Override
	public IInventory asIInventory()
	{
		return this;
	}

	@Override
	public ISidedInventory asSideInventory()
	{
		return this;
	}

	@Override
	public IItemHandlerModifiable asItemHandler()
	{
		return this;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack)
	{
		this.setInventorySlotContents(slot, stack);
	}
}
