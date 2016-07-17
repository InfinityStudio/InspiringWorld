package net.infstudio.inspiringworld.tech.common.item;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechItems {
    public static Item controller = new ItemController().setRegistryName("controller");

    public static void preInit() {
	IWTechItems.registerItem(IWTechItems.controller);
    }

    public static void registerItem(Item item) {
	GameRegistry.register(item);
    }
}