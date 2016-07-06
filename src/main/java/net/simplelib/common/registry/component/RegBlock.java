package net.simplelib.common.registry.component;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
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
		this.getComponent().setUnlocalizedName(getRegisterName());
		GameRegistry.registerBlock(this.getComponent(), this.getRegisterName());
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
//				getComponent().setCreativeTab(tab);
				Item item = Item.getItemFromBlock(getComponent());
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0,
						new ModelResourceLocation(getComponent().getRegistryName(), "inventory"));
				ModelBakery.registerItemVariants(item, getComponent().getRegistryName());
			}
		};
	}
}
