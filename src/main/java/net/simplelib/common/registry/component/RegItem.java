package net.simplelib.common.registry.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.simplelib.common.registry.RegistryHelper;

/**
 * @author CI010
 */
public class RegItem extends RegComponentBase<Item>
{
	public RegItem(String name, Item wrap)
	{
		super(name, wrap);
	}

	@Override
	public RegComponentBase<Item> register()
	{
		this.getComponent().setUnlocalizedName(getRegisterName());
		GameRegistry.registerItem(this.getComponent(), getRegisterName());
		if (getOreName() != null)
			OreDictionary.registerOre(getOreName(), this.getComponent());
		return null;
	}

	@Override
	public Runnable getRegClient()
	{
		return new Runnable()
		{
			@Override
			public void run()
			{
//				this.getComponent().setCreativeTab(tab);
				Item item = getComponent();
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
						0, new ModelResourceLocation(RegistryHelper.INSTANCE.currentMod() +
								":" + getRegisterName(), "inventory"));
				ModelBakery.registerItemVariants(item, new ResourceLocation(RegistryHelper.INSTANCE.currentMod(),
						getRegisterName()));
			}
		};
	}
}
