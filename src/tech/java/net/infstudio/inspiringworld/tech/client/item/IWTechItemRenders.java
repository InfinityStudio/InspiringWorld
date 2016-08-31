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
package net.infstudio.inspiringworld.tech.client.item;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class IWTechItemRenders {

    public static void preInit() {
        registerItemRender(IWTechItems.ABYSS_HELMET);
        registerItemRender(IWTechItems.ABYSS_CHESTPLATE);
        registerItemRender(IWTechItems.ABYSS_LEGGINGS);
        registerItemRender(IWTechItems.ABYSS_BOOTS);

        registerItemRender(IWTechItems.CONTROLLER, 0, "source_controller");
        registerItemRender(IWTechItems.CONTROLLER, 1, "abyss_controller");
        registerItemRender(IWTechItems.SOURCE_BOMB);

        registerItemRender(IWTechBlocks.BLOCK_SOURCE_LIGHT);

        registerItemRender(IWTechBlocks.BLOCK_ANTENNA);
        registerItemRender(IWTechBlocks.BLOCK_ABYSS_ANTENNA);
        registerItemRender(IWTechBlocks.BLOCK_SOURCE_ANTENNA);

        registerItemRender(IWTechBlocks.BLOCK_INSPIRING_FURNACE);
    }

    private static void registerItemRender(Item item, int meta, String location) {
        ResourceLocation rl = new ResourceLocation(item.getRegistryName().getResourceDomain(), location);
        ModelLoader.setCustomModelResourceLocation(item, meta, ModelLoader.getInventoryVariant(rl.toString()));
    }

    private static void registerItemRender(Block block, int meta, String location) {
        registerItemRender(Item.getItemFromBlock(block), meta, location);
    }

    private static void registerItemRender(Item item) {
        registerItemRender(item, 0, item.getRegistryName().getResourcePath());
    }

    private static void registerItemRender(Block block) {
        registerItemRender(block, 0, block.getRegistryName().getResourcePath());
    }
}
