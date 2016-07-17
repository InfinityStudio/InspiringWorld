package net.infstudio.inspiringworld.tech.client.item;

import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class IWTechItemRender {

    public static void preInit() {
	IWTechItemRender.registerItemRender(IWTechItems.controller, 0, "producer_controller");
	IWTechItemRender.registerItemRender(IWTechItems.controller, 1, "consumer_controller");
    }

    private static void registerItemRender(Item item, int meta, String location) {
	ResourceLocation rl = new ResourceLocation(item.getRegistryName().getResourceDomain(), location);
	ModelLoader.setCustomModelResourceLocation(item, meta, ModelLoader.getInventoryVariant(rl.toString()));
    }

    private static void registerItemRender(Block block, int meta, String location) {
	IWTechItemRender.registerItemRender(Item.getItemFromBlock(block), meta, location);
    }

    private static void registerItemRender(Item item) {
	IWTechItemRender.registerItemRender(item, 0, item.getRegistryName().getResourcePath());
    }

    private static void registerItemRender(Block block) {
	IWTechItemRender.registerItemRender(block, 0, block.getRegistryName().getResourcePath());
    }
}
