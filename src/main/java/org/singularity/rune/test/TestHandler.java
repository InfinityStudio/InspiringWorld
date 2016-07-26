package org.singularity.rune.test;

import api.simplelib.registry.ModHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.singularity.rune.MagicRuneMod;

/**
 * @author ci010
 */
@ModHandler
public class TestHandler
{
	@SubscribeEvent
	public void method(PlayerInteractEvent.RightClickEmpty empty)
	{
		System.out.println("mod handler fine!");
		Item test = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(MagicRuneMod.MODID, "test"));
		if (test != null)
			System.out.println("item exist!");
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(MagicRuneMod.MODID, "test"));
		if (block != null)
			System.out.println("block exist!");
	}
}
