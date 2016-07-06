package api.simplelib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author ci010
 */
public class SlotRuled extends Slot
{
	private InventoryRule rule;

	public static List<SlotRuled> of(InventoryElement element)
	{
//		if (element instanceof InventorySlot)
//		{
//			Vector2i pos = element.parent().getLayout().getPos(element.id());
//			return Lists.newArrayList(
//					new SlotRuled(element.parent(), element.id(), pos.getX(), pos.getY(), element.getRule()));
//		}
//		Layout layout = element.parent().getLayout();
//		ArrayList<SlotRuled> slots = Lists.newArrayList();
//		for (int i = element.id(); i < ((InventorySpace) element).getSlots(); i++)
//		{
//			Vector2i pos = layout.getPos(i);
//			slots.add(new SlotRuled(element.parent(), i, pos.getX(), pos.getY(), element.getRule()));
//		}
//		return slots;
		return null;
	}

	public SlotRuled(IInventory inventoryIn, int index, int xPosition, int yPosition, InventoryRule rule)
	{
		super(inventoryIn, index, xPosition, yPosition);
		this.rule = rule;
	}

	@Override
	public void onSlotChanged()
	{
		super.onSlotChanged();
	}

	@Override
	protected void onCrafting(ItemStack stack)
	{
		super.onCrafting(stack);
	}

	@Override
	protected void onCrafting(ItemStack stack, int amount)
	{
		super.onCrafting(stack, amount);
	}

	@Override
	public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_)
	{
		super.onSlotChange(p_75220_1_, p_75220_2_);
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn)
	{
		return rule.isUsebleByPlayer(playerIn);
	}

	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return rule.isItemValid(stack);
	}

	@Override
	public int getSlotStackLimit()
	{
		return rule.getInventoryStackLimit();
	}
}
