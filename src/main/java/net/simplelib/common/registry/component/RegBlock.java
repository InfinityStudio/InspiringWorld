package net.simplelib.common.registry.component;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

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
	public RegComponentBase<Block> register()
	{

		this.getComponent().setRegistryName(getRegisterName());
		if (getComponent().getUnlocalizedName().equals("tile."))
			getComponent().setUnlocalizedName(getRegisterName());
		GameRegistry.register(this.getComponent());
		ItemBlock itemBlock = new ItemBlock(getComponent());
		GameRegistry.register(itemBlock.setRegistryName(getRegisterName()));
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
