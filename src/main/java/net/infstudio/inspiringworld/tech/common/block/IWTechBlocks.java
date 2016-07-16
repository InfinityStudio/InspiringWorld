package net.infstudio.inspiringworld.tech.common.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechBlocks {

    public static void preInit() {
	// TODO
    }

    private void registerBlock(Block block, ItemBlock itemBlock) {
	GameRegistry.register(block);
	GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
    }

    private void registerBlock(Block block) {
	this.registerBlock(block, new ItemBlock(block));
    }
}