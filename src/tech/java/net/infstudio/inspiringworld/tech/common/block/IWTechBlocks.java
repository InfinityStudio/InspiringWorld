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

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class IWTechBlocks {
    public static final IProperty<EnumFacing> FACING = PropertyDirection.create("facing");
    public static final IProperty<Boolean> WORKING = PropertyBool.create("working");

    public static final Material SOURCE_BLOCKS = new Material(MapColor.AIR);

    public static final BlockSourceLight BLOCK_SOURCE_LIGHT = new BlockSourceLight();
    public static final BlockEnderLeaves BLOCK_ENDER_LEAVES = new BlockEnderLeaves();
    public static final BlockEnderSapling BLOCK_ENDER_SAPLING = new BlockEnderSapling();
    public static final BlockEnderVine BLOCK_ENDER_WINE = new BlockEnderVine();
    public static final BlockAntenna BLOCK_ANTENNA = new BlockAntenna();
    public static final BlockSourceAntenna BLOCK_SOURCE_ANTENNA = new BlockSourceAntenna();
    public static final BlockAbyssAntenna BLOCK_ABYSS_ANTENNA = new BlockAbyssAntenna();

    public static void preInit() {
        registerBlock(BLOCK_SOURCE_LIGHT.setRegistryName(InspiringTech.MODID, "source_light"));
        registerBlock(BLOCK_ENDER_LEAVES.setRegistryName(InspiringTech.MODID, "ender_leaves"));
        registerBlock(BLOCK_ENDER_SAPLING.setRegistryName(InspiringTech.MODID, "ender_sapling"));
        registerBlock(BLOCK_ENDER_WINE.setRegistryName(InspiringTech.MODID, "ender_vine"));
        registerBlock(BLOCK_ANTENNA.setRegistryName(InspiringTech.MODID, "antenna"));
        registerBlock(BLOCK_SOURCE_ANTENNA.setRegistryName(InspiringTech.MODID, "source_antenna"));
        registerBlock(BLOCK_ABYSS_ANTENNA.setRegistryName(InspiringTech.MODID, "abyss_antenna"));

        OreDictionary.registerOre("treeLeaves", BLOCK_ENDER_LEAVES);
        OreDictionary.registerOre("vine", BLOCK_ENDER_WINE);
    }

    private static void registerBlock(Block block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
    }

    private static void registerBlock(Block block) {
        registerBlock(block, new ItemBlock(block));
    }
}
