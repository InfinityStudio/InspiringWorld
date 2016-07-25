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
package net.infstudio.inspiringworld.tech.common.crafting;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class IWTechCrafting {

    public static void init() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IWTechItems.controller, 1, 0),
            new Object[] { "###", "# #", "###", '#', "ingotGold" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IWTechItems.controller, 1, 1),
            new Object[] { "###", "# #", "###", '#', "gemLapis" }));
        GameRegistry.addShapedRecipe(new ItemStack(IWTechItems.producerBomb, 8),
            new Object[] { "###", "#*#", "###", '#', Blocks.TNT, '*', new ItemStack(IWTechItems.controller, 1, 0) });
        GameRegistry.addShapedRecipe(new ItemStack(IWTechBlocks.blockSourceLight), new Object[] { "#*#", "*!*", "#*#",
            '#', Items.GLOWSTONE_DUST, '*', Items.REDSTONE, '!', new ItemStack(IWTechItems.controller, 1, 0) });
        GameRegistry.addShapedRecipe(new ItemStack(IWTechBlocks.blockSourceLight), new Object[] { "*#*", "#!#", "*#*",
            '#', Items.GLOWSTONE_DUST, '*', Items.REDSTONE, '!', new ItemStack(IWTechItems.controller, 1, 0) });
    }
}
