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
package net.infstudio.inspiringworld.tech;

import net.infstudio.inspiringworld.tech.common.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = InspiringTech.MODID, version = InspiringTech.VERSION, acceptedMinecraftVersions = InspiringTech.MC_VERSION, dependencies = InspiringTech.DEPENDENCIES)
public class InspiringTech {
    public static final String MODID = "inspiringtech";
    public static final String VERSION = "@version@";
    public static final String MC_VERSION = "1.10.2";
    public static final String DEPENDENCIES = ""; // "required-after:inspiringworld";

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
