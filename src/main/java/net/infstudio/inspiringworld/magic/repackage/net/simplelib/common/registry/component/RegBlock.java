package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterators;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @author CI010
 */
public class RegBlock extends RegComponentBase<Block>
{
	public RegBlock(String name, Block wrap)
	{
		super(name, wrap);
	}

	@Override
	protected String handleRegisterName(String name)
	{
		String s = super.handleRegisterName(name);
		String[] split = s.split("_");
		while (ArrayUtils.contains(split, "block"))
			split = ArrayUtils.removeElement(split, "block");
		return Joiner.on('_').join(split);
	}

	@Override
	public RegComponentBase<Block> register()
	{
		if (this.getComponent().getRegistryName() == null)
			this.getComponent().setRegistryName(getRegisterName());
		if (getComponent().getUnlocalizedName().equals("tile."))
			getComponent().setUnlocalizedName(getRegisterName());
		GameRegistry.register(this.getComponent());
		ItemBlock itemBlock = new ItemBlock(getComponent());
		GameRegistry.register(itemBlock.setRegistryName("itemblock_".concat(getRegisterName())));
		if (this.getOreName() != null)
			OreDictionary.registerOre(this.getOreName(), this.getComponent());
		return this;
	}

	@Override
	public Runnable getRegClient()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
				if (getComponent().getCreativeTabToDisplayOn() == null)
					getComponent().setCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[getCreativeTabId()]);
				Item item = Item.getItemFromBlock(getComponent());
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
						new ModelResourceLocation(getComponent().getRegistryName(), "inventory"));
				ModelBakery.registerItemVariants(item, getComponent().getRegistryName());
			}
		};
	}
}
