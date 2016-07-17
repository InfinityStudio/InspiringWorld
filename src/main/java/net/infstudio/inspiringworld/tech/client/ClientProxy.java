package net.infstudio.inspiringworld.tech.client;

import net.infstudio.inspiringworld.tech.client.item.IWTechItemRender;
import net.infstudio.inspiringworld.tech.common.CommonProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
	super.preInit(event);

	IWTechItemRender.preInit();
    }

    @Override
    public void init(FMLInitializationEvent event) {
	super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
	super.postInit(event);
    }

    public static <T extends FMLStateEvent> T event() {
	return CommonProxy.event();
    }
}
