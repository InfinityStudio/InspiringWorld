package net.infstudio.inspiringworld.tech.common;

import java.lang.ref.WeakReference;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.config.IWTechConfig;
import net.infstudio.inspiringworld.tech.common.crafting.IWTechCrafting;
import net.infstudio.inspiringworld.tech.common.creativetab.IWTechCreativeTabs;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class CommonProxy {

    protected static WeakReference<FMLStateEvent> event;

    public void preInit(FMLPreInitializationEvent event) {
	CommonProxy.event = new WeakReference<FMLStateEvent>(event);

	IWTechConfig.preInit();
	IWTechItems.preInit();
	IWTechBlocks.preInit();
	IWTechCreativeTabs.preInit();
    }

    public void init(FMLInitializationEvent event) {
	CommonProxy.event = new WeakReference<FMLStateEvent>(event);

	IWTechCrafting.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
	CommonProxy.event = new WeakReference<FMLStateEvent>(event);
    }

    public static <T extends FMLStateEvent> T event()
    {
	@SuppressWarnings("unchecked")
	T result = (T) CommonProxy.event.get();
	return result;
    }
}
