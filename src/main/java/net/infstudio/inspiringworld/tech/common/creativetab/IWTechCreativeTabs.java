package net.infstudio.inspiringworld.tech.common.creativetab;

import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class IWTechCreativeTabs {
    public static CreativeTabs general;

    public static void preInit() {
	IWTechItems.controller.setCreativeTab(IWTechCreativeTabs.general);
    }

    static {
	IWTechCreativeTabs.general = new CreativeTabs("inspiringtech") {
	    @Override
	    public Item getTabIconItem() {
		return IWTechItems.controller;
	    }
	};
    }
}
