package net.infstudio.inspiringworld.tech.common.item;

import java.util.List;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemController extends Item {

    public ItemController() {
	this.setMaxDamage(0);
	this.setHasSubtypes(true);
	this.setMaxStackSize(16);
	this.setUnlocalizedName(InspiringTech.MODID + "." + "controller");
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
	subItems.add(new ItemStack(this, 1, 0));
	subItems.add(new ItemStack(this, 1, 1));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
	int i = stack.getMetadata();
	return super.getUnlocalizedName() + "." + (i == 0 ? "producer" : "consumer");
    }
}
