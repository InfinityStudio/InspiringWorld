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
package net.infstudio.inspiringworld.tech.common.creativetab;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class IWTechCreativeTabs {
    public static final CreativeTabs GENERAL = new CreativeTabs("inspiringtech") {
        @Override
        public Item getTabIconItem() {
            return IWTechItems.CONTROLLER;
        }
    };

    public static void preInit() {
        IWTechItems.CONTROLLER.setCreativeTab(GENERAL);
        IWTechItems.SOURCE_BOMB.setCreativeTab(GENERAL);

        IWTechItems.ABYSS_HELMET.setCreativeTab(GENERAL);
        IWTechItems.ABYSS_CHESTPLATE.setCreativeTab(GENERAL);
        IWTechItems.ABYSS_LEGGINGS.setCreativeTab(GENERAL);
        IWTechItems.ABYSS_BOOTS.setCreativeTab(GENERAL);

        IWTechBlocks.BLOCK_SOURCE_LIGHT.setCreativeTab(GENERAL);

        IWTechBlocks.BLOCK_ANTENNA.setCreativeTab(GENERAL);
        IWTechBlocks.BLOCK_SOURCE_ANTENNA.setCreativeTab(GENERAL);
        IWTechBlocks.BLOCK_ABYSS_ANTENNA.setCreativeTab(GENERAL);

        IWTechBlocks.BLOCK_INSPIRING_FURNACE.setCreativeTab(GENERAL);
    }
}
