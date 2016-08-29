package net.infstudio.inspiringworld.tech.common.capability;

import net.infstudio.inspiringworld.tech.api.energy.network.INetworkGraphVertexBase;
import net.infstudio.inspiringworld.tech.common.capability.CapabilityIWTechEnergy.StorageEmpty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class IWTechCapablilties {

    /* They will be injected. */

    /* TODO
    @CapabilityInject(IIWTechEnergyConsumer.class)
    public static final Capability<IIWTechEnergyConsumer> TECH_ENERGY_CONSUMER = null;

    @CapabilityInject(IIWTechSourceEnergyProducer.class)
    public static final Capability<IIWTechSourceEnergyProducer> TECH_SOURCE_ENERGY_PRODUCER = null;

    @CapabilityInject(IIWTechAbyssEnergyProducer.class)
    public static final Capability<IIWTechAbyssEnergyProducer> TECH_ABYSS_ENERGY_PRODUCER = null;
     */
    @CapabilityInject(INetworkGraphVertexBase.class)
    public static final Capability<INetworkGraphVertexBase> NETWORK_GRAPH_VERTEX = null;

    public static void preInit() {
        CapabilityManager.INSTANCE.register(INetworkGraphVertexBase.class, new StorageEmpty<INetworkGraphVertexBase>(),
            INetworkGraphVertexBase.class);
    }
}
