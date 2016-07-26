package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory.legacy;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory.InventoryRule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * A base implementation of {@link IInventory} and {@link ISidedInventory}.
 *
 * @author ci010
 */
public class InventoryBase extends InventoryBasic implements ISidedInventory
{
	private EnumMap<EnumFacing, int[]> faceSlots;
	private InventoryRule[] rules;
	private boolean singleRule;

	public static IInventory newInventory(int size)
	{
		return new InventoryBase(size);
	}

	public static IInventory newInventory(int slotCount, InventoryRule rule)
	{
		InventoryBase inventoryBase = new InventoryBase(slotCount);
		inventoryBase.singleRule = true;
		inventoryBase.rules = new InventoryRule[]{rule};
		return inventoryBase;
	}

	public static IInventory newInventory(int slotCount, InventoryRule[] rules)
	{
		InventoryBase inv = new InventoryBase(slotCount);
		inv.rules = Arrays.copyOf(rules, rules.length);
		return inv;
	}

	public static ISidedInventory newInventoryWithSide(EnumFacing side, int slotCount)
	{
		return newInventoryWithSide(side, slotCount, InventoryRule.COMMON);
	}

	public static ISidedInventory newInventoryWithSide(EnumFacing side, int slotCount, InventoryRule rule)
	{
		return newInventoryWithSide(side, slotCount, new InventoryRule[]{rule});
	}

	public static ISidedInventory newInventoryWithSide(EnumFacing side, int slotCount, InventoryRule[] rules)
	{
		InventoryBase inv = new InventoryBase(slotCount);
		if (rules != null)
			inv.rules = Arrays.copyOf(rules, rules.length);
		if (side != null)
		{
			int[] ints = new int[inv.getSizeInventory()];
			for (int i = 0; i < ints.length; i++)
				ints[i] = i;
			inv.faceSlots = new EnumMap<EnumFacing, int[]>(EnumFacing.class);
			inv.faceSlots.put(side, ints);
		}
		return inv;
	}

	public static ISidedInventory newInventoryWithSides(EnumMap<EnumFacing, int[]> sideMap, int slotCount)
	{
		InventoryBase inv = new InventoryBase(slotCount);
		inv.faceSlots = sideMap.clone();
		return inv;
	}

	public static ISidedInventory newInventoryWithSides(EnumMap<EnumFacing, int[]> sideMap, int slotCount, InventoryRule[] rules)
	{
		InventoryBase inv = new InventoryBase(slotCount);
		if (rules != null)
			inv.rules = Arrays.copyOf(rules, rules.length);
		inv.faceSlots = sideMap.clone();
		return inv;
	}

	public static ISidedInventory newInventoryWithSides(EnumMap<EnumFacing, int[]> sideMap, int slotCount, InventoryRule rule)
	{
		InventoryBase inv = new InventoryBase(slotCount);
		inv.singleRule = true;
		inv.rules = new InventoryRule[]{rule};
		inv.faceSlots = sideMap.clone();
		return inv;
	}

	private InventoryBase(int slotCount)
	{
		super("inv", false, slotCount);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		if (faceSlots == null)
		{
			int[] ints = new int[this.getSizeInventory()];
			for (int i = 0; i < ints.length; i++)
				ints[i] = i;
			return ints;
		}
		return Arrays.copyOf(faceSlots.get(side), faceSlots.get(side).length);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		int slot = SidedInvWrapper.getSlot(this, index, direction);
		if (slot == -1)
			return false;
		if (rules == null)
			return true;
		if (singleRule)
			return rules[0].isItemValid(itemStackIn);
		return rules[slot].isItemValid(itemStackIn);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return rules[0].isUsebleByPlayer(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return rules[index].isItemValid(stack);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		int slot = SidedInvWrapper.getSlot(this, index, direction);
		return slot != -1;
	}
}
