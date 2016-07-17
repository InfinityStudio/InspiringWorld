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

    public static <T extends FMLStateEvent> T event() {
        @SuppressWarnings("unchecked")
        T result = (T) CommonProxy.event.get();
        return result;
    }
}
