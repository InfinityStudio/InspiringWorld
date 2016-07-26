package net.infstudio.inspiringworld.magic.repackage.api.simplelib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * The rule of an inventory space should fallow.
 * <p>The inventory space could be a slot or multiple slots created by {@link Inventory}.</p>
 *
 * @author ci010
 * @see Inventory
 */
public interface InventoryRule
{
	/**
	 * @param player the player.
	 * @return if a player should use this inventory.
	 */
	boolean isUsebleByPlayer(EntityPlayer player);

	/**
	 * @param stack the new item stack.
	 * @return if a item stack could place on this inventory.
	 */
	boolean isItemValid(ItemStack stack);

	/**
	 * @return The maximum size of item stack could be.
	 */
	int getInventoryStackLimit();

	InventoryRule COMMON = new InventoryRule()
	{
		@Override
		public boolean isUsebleByPlayer(EntityPlayer player)
		{
			return true;
		}

		@Override
		public boolean isItemValid(ItemStack stack)
		{
			return true;
		}

		@Override
		public int getInventoryStackLimit()
		{
			return 64;
		}
	};
}
