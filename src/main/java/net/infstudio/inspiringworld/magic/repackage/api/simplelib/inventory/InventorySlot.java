package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory;

import net.minecraft.item.ItemStack;


/**
 * @author ci010
 */
public interface InventorySlot extends InventoryElement
{
	/**
	 * Returns the ItemStack in a given slot.
	 * <p/>
	 * The result's stack size may be greater than the itemstacks max size.
	 * <p/>
	 * If the result is null, then the slot is empty.
	 * If the result is not null but the stack size is zero, then it represents
	 * an empty slot that will only accept* a specific itemstack.
	 * <p/>
	 * <p/>
	 * IMPORTANT: This ItemStack MUST NOT be modified. This method is not for
	 * altering an inventories contents. Any implementers who are able to detect
	 * modification through this method should throw an exception.
	 * <p/>
	 * SERIOUSLY: DO NOT MODIFY THE RETURNED ITEMSTACK
	 *
	 * @return ItemStack in given slot. May be null.
	 **/
	ItemStack getStackInSlot();

	/**
	 * Inserts an ItemStack into the given slot and return the remainder.
	 * Note: This behaviour is subtly different from IFluidHandlers.fill()
	 *
	 * @param stack    ItemStack to insert
	 * @param simulate If true, the insertion is only simulated
	 * @return The remaining ItemStack that was not inserted (if the entire stack is accepted, then return null)
	 **/
	ItemStack insertItem(ItemStack stack, boolean simulate);

	/**
	 * Extracts an ItemStack from the given slot. The returned value must be null
	 * if nothing is extracted, otherwise it's stack size must not be greater than amount or the
	 * itemstacks getMaxStackSize().
	 *
	 * @param amount   Amount to extract (may be greater than the current stacks max limit)
	 * @param simulate If true, the extraction is only simulated
	 * @return ItemStack extracted from the slot, must be null, if nothing can be extracted
	 **/
	ItemStack extractItem(int amount, boolean simulate);
}
