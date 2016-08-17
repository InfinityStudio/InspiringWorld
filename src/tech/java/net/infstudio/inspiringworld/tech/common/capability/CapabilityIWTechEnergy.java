package net.infstudio.inspiringworld.tech.common.capability;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechAbyssEnergyProducer;
import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyConsumer;
import net.infstudio.inspiringworld.tech.api.energy.IIWTechSourceEnergyProducer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityIWTechEnergy {
    public static class StorageConsumer implements IStorage<IIWTechEnergyConsumer> {
        @Override
        public NBTBase writeNBT(Capability<IIWTechEnergyConsumer> capability, IIWTechEnergyConsumer instance,
            EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("IWTechEnergyConsumer", instance.getEnergySpace());
            return compound;
        }

        @Override
        public void readNBT(Capability<IIWTechEnergyConsumer> capability, IIWTechEnergyConsumer instance,
            EnumFacing side, NBTBase nbt) {
            int value = ((NBTTagCompound) nbt).getInteger("IWTechEnergyConsumer");
            instance.setEnergySpace(value);
        }
    }

    public static class StorageSourceProducer implements IStorage<IIWTechSourceEnergyProducer> {
        @Override
        public NBTBase writeNBT(Capability<IIWTechSourceEnergyProducer> capability,
            IIWTechSourceEnergyProducer instance, EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("IWTechSourceEnergyProducer", instance.getContainedSourceEnergy());
            return compound;
        }

        @Override
        public void readNBT(Capability<IIWTechSourceEnergyProducer> capability, IIWTechSourceEnergyProducer instance,
            EnumFacing side, NBTBase nbt) {
            int value = ((NBTTagCompound) nbt).getInteger("IWTechSourceEnergyProducer");
            instance.setContainedSourceEnergy(value);
        }
    }

    public static class StorageAbyssProducer implements IStorage<IIWTechAbyssEnergyProducer> {
        @Override
        public NBTBase writeNBT(Capability<IIWTechAbyssEnergyProducer> capability, IIWTechAbyssEnergyProducer instance,
            EnumFacing side) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setInteger("IWTechAbyssEnergyProducer", instance.getContainedAbyssEnergy());
            return compound;
        }

        @Override
        public void readNBT(Capability<IIWTechAbyssEnergyProducer> capability, IIWTechAbyssEnergyProducer instance,
            EnumFacing side, NBTBase nbt) {
            int value = ((NBTTagCompound) nbt).getInteger("IWTechAbyssEnergyProducer");
            instance.setContainedAbyssEnergy(value);
        }
    }
}
