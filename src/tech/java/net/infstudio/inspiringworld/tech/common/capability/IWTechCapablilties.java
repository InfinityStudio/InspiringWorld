package net.infstudio.inspiringworld.tech.common.capability;

import net.infstudio.inspiringworld.tech.api.energy.IIWTechAbyssEnergyProducer;
import net.infstudio.inspiringworld.tech.api.energy.IIWTechEnergyConsumer;
import net.infstudio.inspiringworld.tech.api.energy.IIWTechSourceEnergyProducer;
import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertexBase;
import net.infstudio.inspiringworld.tech.common.capability.CapabilityIWTechEnergy.StorageEmpty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class IWTechCapablilties {

    /* They will be injected. */

    @CapabilityInject(INetworkGraphVertexBase.class)
    public static final Capability<INetworkGraphVertexBase> NETWORK_GRAPH_VERTEX = null;

    @CapabilityInject(IIWTechEnergyConsumer.class)
    public static final Capability<IIWTechEnergyConsumer> ENERGY_CONSUMER = null;

    @CapabilityInject(IIWTechAbyssEnergyProducer.class)
    public static final Capability<IIWTechAbyssEnergyProducer> ABYSS_ENERGY_PRODUCER = null;

    @CapabilityInject(IIWTechSourceEnergyProducer.class)
    public static final Capability<IIWTechSourceEnergyProducer> SOURCE_ENERGY_PRODUCER = null;

    public static void preInit() {
        CapabilityManager.INSTANCE.register(INetworkGraphVertexBase.class, new StorageEmpty<INetworkGraphVertexBase>(),
            INetworkGraphVertexBase.class);
        CapabilityManager.INSTANCE.register(IIWTechEnergyConsumer.class, new StorageEmpty<IIWTechEnergyConsumer>(),
            IIWTechEnergyConsumer.class);
        CapabilityManager.INSTANCE.register(IIWTechAbyssEnergyProducer.class,
            new StorageEmpty<IIWTechAbyssEnergyProducer>(), IIWTechAbyssEnergyProducer.class);
        CapabilityManager.INSTANCE.register(IIWTechSourceEnergyProducer.class,
            new StorageEmpty<IIWTechSourceEnergyProducer>(), IIWTechSourceEnergyProducer.class);
    }
}
