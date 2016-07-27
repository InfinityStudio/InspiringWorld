package net.infstudio.inspiringworld.tech.common.capability;

import net.infstudio.inspiringworld.tech.common.capability.CapabilityInspiring.IInspiringTechHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityInspiring implements IStorage<IInspiringTechHandler> {
    public static interface IInspiringTechHandler {
        // TODO: handle source and abyss
    }

    @Override
    public NBTBase writeNBT(Capability<IInspiringTechHandler> capability, IInspiringTechHandler instance,
        EnumFacing side) {
        // TODO: Auto-generated method stub
        return null;
    }

    @Override
    public void readNBT(Capability<IInspiringTechHandler> capability, IInspiringTechHandler instance, EnumFacing side,
        NBTBase nbt) {
        // TODO: Auto-generated method stub
    }
}
