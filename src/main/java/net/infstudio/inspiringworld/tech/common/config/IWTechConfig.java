
package net.infstudio.inspiringworld.tech.common.config;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class IWTechConfig {

    private static Logger logger;

    private static File configDir;

    private static Configuration general;

    public static void preInit() {
	FMLPreInitializationEvent event = CommonProxy.event();
	IWTechConfig.logger = event.getModLog();

	IWTechConfig.configDir = new File(event.getModConfigurationDirectory(), InspiringTech.MODID);

	IWTechConfig.general = new Configuration(new File(IWTechConfig.configDir, "general.cfg"));
	IWTechConfig.general.load();

	IWTechConfig.loadConfig();
    }

    private static void loadConfig() {
	// TODO

	IWTechConfig.general.save();
    }

    public static Logger logger() {
	return IWTechConfig.logger;
    }
}
