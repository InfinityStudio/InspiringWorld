package org.singularity.rune;

import api.simplelib.registry.ModHandler;
import api.simplelib.registry.components.ModComponent;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

/**
 * @author ci010
 */
@Mod(modid = MagicRuneMod.MODID, name = MagicRuneMod.NAME, version = MagicRuneMod.VERSION)
public class MagicRuneMod
{
	public static final String MODID = "singularity_rune", NAME = "Singularity Rune", VERSION = "0.0.1";

	@ModHandler
	public static class TestHandler
	{
		@SubscribeEvent
		public void method(PlayerInteractEvent.RightClickEmpty empty)
		{
			System.out.println("mod handler fine!");
			Item test = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(MODID, "test"));
			if (test != null)
				System.out.println("item exist!");
			Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(MODID, "test"));
			if (block != null)
				System.out.println("block exist!");
		}
	}

	@ModComponent
	public static class BlockTest extends Block
	{
		public BlockTest()
		{
			super(Material.ROCK);
		}
	}

	@ModComponent
	public static class ItemTest extends Item
	{

	}
}
