package net.infstudio.inspiringworld.tech.common.crafting;

import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class IWTechCrafting {

    public static void init() {
	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IWTechItems.controller, 1, 0),
		new Object[] { "###", "# #", "###", '#', "ingotGold" }));
	GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(IWTechItems.controller, 1, 1),
		new Object[] { "###", "# #", "###", '#', "gemLapis" }));
    }
}
