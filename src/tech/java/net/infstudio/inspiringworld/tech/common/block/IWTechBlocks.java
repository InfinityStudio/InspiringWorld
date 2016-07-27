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
package net.infstudio.inspiringworld.tech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechBlocks {
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final IProperty<Boolean> WORKING = PropertyBool.create("working");

    public static final Material SOURCE_BLOCKS = new Material(MapColor.AIR);

    public static final Block blockSourceLight = new BlockSourceLight();

    public static void preInit() {
        IWTechBlocks.registerBlock(IWTechBlocks.blockSourceLight.setRegistryName("source_light"));
    }

    private static void registerBlock(Block block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
    }

    private static void registerBlock(Block block) {
        IWTechBlocks.registerBlock(block, new ItemBlock(block));
    }
}
