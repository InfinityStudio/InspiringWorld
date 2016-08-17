package net.infstudio.inspiringworld.tech.common.capability;

import com.google.common.base.Preconditions;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyConsumer;
import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyProducer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityIWTechEnergy {
    public static class StorageConsumer implements IStorage<IIWTechEnergyConsumer> {
        // do nothing
        @Override
        public NBTBase writeNBT(Capability<IIWTechEnergyConsumer> capability, IIWTechEnergyConsumer instance,
            EnumFacing side) {
            return new NBTTagString("IWTechEnergyConsumer");
        }

        @Override
        public void readNBT(Capability<IIWTechEnergyConsumer> capability, IIWTechEnergyConsumer instance,
            EnumFacing side, NBTBase nbt) {
            Preconditions.checkArgument("IWTechEnergyConsumer".equals(((NBTTagString) nbt).getString()));
        }
    }

    public static class StorageProducer implements IStorage<IIWTechEnergyProducer> {
        // do nothing
        @Override
        public NBTBase writeNBT(Capability<IIWTechEnergyProducer> capability, IIWTechEnergyProducer instance,
            EnumFacing side) {
            return new NBTTagString("IWTechEnergyProducer");
        }

        @Override
        public void readNBT(Capability<IIWTechEnergyProducer> capability, IIWTechEnergyProducer instance,
            EnumFacing side, NBTBase nbt) {
            Preconditions.checkArgument("IWTechEnergyProducer".equals(((NBTTagString) nbt).getString()));
        }
    }
}
