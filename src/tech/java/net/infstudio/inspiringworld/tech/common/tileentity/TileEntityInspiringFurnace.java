package net.infstudio.inspiringworld.tech.common.tileentity;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyConsumer;
import net.infstudio.inspiringworld.tech.common.capability.IWTechCapablilties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityInspiringFurnace extends TileEntity implements ITickable {

    protected ItemStackHandler inputStack = new ItemStackHandler();
    protected ItemStackHandler outputStack = new ItemStackHandler();

    protected EnergyConsumer consumer = new EnergyConsumer();

    // 0 to 1
    protected float progress = 0;

    protected int energyContained = 0;

    public class EnergyConsumer implements IIWTechEnergyConsumer {

        public static final int TOTAL = 2000;

        @Override
        public int getEnergySpace() {
            return TOTAL - TileEntityInspiringFurnace.this.energyContained;
        }

        @Override
        public void setEnergySpace(int energy) {
            TileEntityInspiringFurnace.this.energyContained = MathHelper.clamp_int(TOTAL - energy, 0, TOTAL);
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = MathHelper.clamp_int(maxReceive, 0, TOTAL - TileEntityInspiringFurnace.this.energyContained);
            if (!simulate) {
                TileEntityInspiringFurnace.this.energyContained += received;
            }
            return maxReceive - received;
        }

        public float getProgress() {
            return TileEntityInspiringFurnace.this.progress;
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (IWTechCapablilties.ENERGY_CONSUMER.equals(capability)) {
            return true;
        }
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (IWTechCapablilties.ENERGY_CONSUMER.equals(capability)) {
            return IWTechCapablilties.ENERGY_CONSUMER.cast(this.consumer);
        }
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.equals(capability)) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                .cast(EnumFacing.DOWN.equals(facing) ? this.outputStack : this.inputStack);
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void update() {
        if (this.worldObj.isRemote) {
            return;
        } else if (this.energyContained > 0) {
            this.energyContained -= 1;
            this.progress += 0.005f;
            if (this.progress >= 1) {
                ItemStack inputStack = this.inputStack.extractItem(0, 1, true);
                if (inputStack != null) {
                    ItemStack outputStack = FurnaceRecipes.instance().getSmeltingResult(inputStack);
                    if (this.outputStack.insertItem(0, outputStack, true) == null) {
                        inputStack = this.inputStack.extractItem(0, 1, false);
                        outputStack = FurnaceRecipes.instance().getSmeltingResult(inputStack);
                        this.outputStack.insertItem(0, outputStack, false);
                        this.progress = 0;
                        return;
                    }
                }
                this.progress = 1;
            }
        } else {
            this.progress = Math.max(0, this.progress - 0.025f);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        this.inputStack.deserializeNBT(compound.getCompoundTag("UpInventory"));
        this.outputStack.deserializeNBT(compound.getCompoundTag("DownInventory"));
        this.energyContained = compound.getInteger("EnergyContained");
        this.progress = compound.getFloat("ProgressBar");
        super.readFromNBT(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("UpInventory", this.inputStack.serializeNBT());
        compound.setTag("DownInventory", this.outputStack.serializeNBT());
        compound.setInteger("EnergyContained", this.energyContained);
        compound.setFloat("ProgressBar", this.progress);
        return super.writeToNBT(compound);
    }

}
