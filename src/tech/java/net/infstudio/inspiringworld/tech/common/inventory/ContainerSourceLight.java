package net.infstudio.inspiringworld.tech.common.inventory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerSourceLight extends Container {
    private final Optional<TileEntitySourceLight> tileEntity;

    private final IItemHandler burningSlot;

    public ContainerSourceLight(EntityPlayer player, IItemHandler burningSlot, TileEntitySourceLight tileEntity) {
        this.tileEntity = Optional.fromNullable(tileEntity);
        this.burningSlot = Preconditions.checkNotNull(burningSlot);

        this.addSlotToContainer(new SlotItemHandler(this.burningSlot, 0, 56, 51) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return TileEntityFurnace.isItemFuel(stack) && super.isItemValid(stack);
            }
        });

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !this.tileEntity.isPresent()
            || this.tileEntity.get().getPos().distanceSqToCenter(playerIn.posX, playerIn.posY, playerIn.posZ) <= 64.0;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack oldStack = null;
        Slot slot = this.inventorySlots.get(index);
        // 0: burning slot
        // 1-27: 27 player inventory slot
        // 28-36: 9 other player inventory slot
        if (slot != null && slot.getHasStack()) {
            ItemStack mergedStack = slot.getStack();
            oldStack = mergedStack.copy();

            if (index == 0) {
                if (!this.mergeItemStack(mergedStack, 1, 37, true)) {
                    return null;
                }
            } else if (TileEntityFurnace.isItemFuel(mergedStack)) {
                if (!this.mergeItemStack(mergedStack, 0, 1, false)) {
                    return null;
                }
            } else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(mergedStack, 28, 37, false)) {
                    return null;
                }
            } else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(mergedStack, 1, 28, false)) {
                    return null;
                }
            }

            if (mergedStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
        }
        return oldStack;
    }
}
