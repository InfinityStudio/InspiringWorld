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
package net.infstudio.inspiringworld.tech.common.item;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechItems {
    public static Item controller = new ItemController().setRegistryName(InspiringTech.MODID, "controller");
    public static Item producerBomb = new ItemSourceBomb().setRegistryName(InspiringTech.MODID, "producer_bomb");

    public static void preInit() {
        IWTechItems.registerItem(IWTechItems.controller);
        IWTechItems.registerItem(IWTechItems.producerBomb);
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }
}
