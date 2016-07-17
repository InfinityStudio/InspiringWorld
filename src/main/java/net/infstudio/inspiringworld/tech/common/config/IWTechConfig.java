/**
 * InspiringWorld Mod for Minecraft.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.infstudio.inspiringworld.tech.common.config;

import java.io.File;

import org.apache.logging.log4j.Logger;

import net.infstudio.inspiringworld.core.InspiringWorld;
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

        IWTechConfig.configDir = new File(event.getModConfigurationDirectory(), InspiringWorld.MODID);

        IWTechConfig.general = new Configuration(new File(IWTechConfig.configDir, "tech.cfg"));
        IWTechConfig.general.load();

        IWTechConfig.loadConfig();
    }

    private static void loadConfig() {
        // TODO Load configuration from file here
        IWTechConfig.general.save();
    }

    public static Logger logger() {
        return IWTechConfig.logger;
    }
}
