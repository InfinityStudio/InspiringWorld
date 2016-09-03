package net.infstudio.inspiringworld.magic.common;

import net.infstudio.inspiringworld.magic.world.gen.WorldGenRemains;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        GameRegistry.registerWorldGenerator(new WorldGenRemains(),1);
    }

    public void init(FMLInitializationEvent event) {
        
    }

    public void postInit(FMLPostInitializationEvent event) {
        
    }
}
