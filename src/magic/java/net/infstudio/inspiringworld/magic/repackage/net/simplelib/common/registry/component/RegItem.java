package net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.component;

import com.google.common.base.Joiner;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.registry.RegistryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.ArrayUtils;

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
	protected String handleRegisterName(String name)
	{
		String s = super.handleRegisterName(name);
		String[] split = s.split("_");
		while (ArrayUtils.contains(split, "item"))
			split = ArrayUtils.removeElement(split, "item");
		return Joiner.on('_').join(split);
	}

	@Override
	public RegComponentBase<Item> register()
	{
		if (getComponent().getUnlocalizedName().equals("tile."))
			getComponent().setUnlocalizedName(getRegisterName());
		if (getComponent().getRegistryName() == null)
			this.getComponent().setRegistryName(getRegisterName());
		GameRegistry.register(this.getComponent());
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
				Item item = getComponent();
				if (getComponent().getCreativeTab() == null)
					item.setCreativeTab(CreativeTabs.CREATIVE_TAB_ARRAY[getCreativeTabId()]);
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item,
						0, new ModelResourceLocation(RegistryHelper.INSTANCE.currentMod() + ":" + getRegisterName(), "inventory"));
				ModelBakery.registerItemVariants(item, new ResourceLocation(RegistryHelper.INSTANCE.currentMod(), getRegisterName()));
			}
		};
	}
}
