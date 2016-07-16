package net.infstudio.inspiringworld.tech;

import net.infstudio.inspiringworld.tech.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = InspiringTech.MODID, version = InspiringTech.VERSION, acceptedMinecraftVersions = "1.10.2")
public class InspiringTech {
    public static final String MODID = "inspiringtech";
    public static final String VERSION = "@version@";

    @Instance(InspiringTech.MODID)
    public static InspiringTech instance;

    public static final String CLIENT_PROXY = "net.infstudio.inspiringworld.tech.client.ClientProxy";
    public static final String COMMON_PROXY = "net.infstudio.inspiringworld.tech.common.CommonProxy";

    @SidedProxy(clientSide = InspiringTech.CLIENT_PROXY, serverSide = InspiringTech.COMMON_PROXY)
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
	InspiringTech.proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
	InspiringTech.proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
	InspiringTech.proxy.postInit(event);
    }
}
