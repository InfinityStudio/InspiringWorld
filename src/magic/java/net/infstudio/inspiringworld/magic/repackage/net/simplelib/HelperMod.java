package net.infstudio.inspiringworld.magic.repackage.net.simplelib;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils.Environment;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.RegistryBufferManager;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.RegistryHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

@Mod(modid = HelperMod.MODID, name = HelperMod.NAME, version = HelperMod.VERSION, useMetadata = true)
public class HelperMod
{
	public static final String MODID = "simple_lib", NAME = "Simple Lib", VERSION = "beta 0.8";

	@Mod.Metadata(MODID)
	public static ModMetadata metadata;

	@Mod.Instance(MODID)
	public static HelperMod instance;

	@SidedProxy(modId = MODID, serverSide = "net.infstudio.inspiringworld.magic.repackage.net.simplelib.CommonProxy", clientSide = "net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.ClientProxy")
	public static CommonProxy proxy;

	@Mod.EventHandler
	public void construct(FMLConstructionEvent event)
	{
		RegistryBufferManager.instance().load(event.getASMHarvestedData());
		RegistryBufferManager.instance().invoke(event);
	}

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		if (Environment.debug())
			DebugLogger.info("Detected that this is a development environment. Debug mode on.");
		RegistryBufferManager.instance().invoke(event);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
//		metadata.autogenerated = false;
//		metadata.description = "A mod that can simplify the mod	 registrant.";
//		metadata.url = "https://github.com/ci010/SimpleLib";
//		metadata.authorList.add(0, "ci010");
//		metadata.updateUrl = "https://github.com/ci010/SimpleLib/releases";
		//TODO remove this test code
//		RegistryHelper.INSTANCE.registerSittableBlock(Blocks.brick_stairs);
		RegistryBufferManager.instance().invoke(event);
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {RegistryBufferManager.instance().invoke(event);}

	@Mod.EventHandler
	public void complete(FMLLoadCompleteEvent event)
	{
		RegistryBufferManager.instance().invoke(event);
		RegistryHelper.INSTANCE.close();
	}

	@Mod.EventHandler
	public void serverAboutStart(FMLServerAboutToStartEvent event) {RegistryBufferManager.instance().invoke(event);}

	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		RegistryBufferManager.instance().invoke(event);
	}

	@Mod.EventHandler
	public void serverStarted(FMLServerStartedEvent event)
	{
		RegistryBufferManager.instance().invoke(event);
	}

	@NetworkCheckHandler
	public boolean acceptModList(Map<String, String> modList, Side side)
	{
		return ModRestriction.acceptModList(modList, side);
	}
}
