package net.infstudio.inspiringworld.tech.common.tileentity;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Blealtan
 */
public class TileEntitySourceLight extends TileEntity implements ITickable {
    protected int burnTime = 0;

    protected ItemStackHandler inventory = new ItemStackHandler();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)
            || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
            @SuppressWarnings("unchecked")
            T result = (T) inventory;
            return result;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.inventory.deserializeNBT(compound.getCompoundTag("Inventory"));
        this.burnTime = compound.getInteger("BurnTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setTag("Inventory", this.inventory.serializeNBT());
        compound.setInteger("BurnTime", this.burnTime);
        return compound;
    }

    @Override
    public void update() {
        if (!this.worldObj.isRemote) {
            IBlockState state = this.worldObj.getBlockState(pos);
            if (burnTime != 0)
                burnTime--;
            if (burnTime == 0) {
                if (inventory.extractItem(0, 1, true) != null) {
                    burnTime = 600; // TODO: Change this to fuel corresponding time
                    inventory.extractItem(0, 1, false);
                    this.worldObj.setBlockState(pos, state.withProperty(IWTechBlocks.WORKING, true));
                } else {
                    this.worldObj.setBlockState(pos, state.withProperty(IWTechBlocks.WORKING, false));
                }
            }
        }
    }
}
