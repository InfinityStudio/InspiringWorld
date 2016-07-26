package net.infstudio.inspiringworld.magic.test;

import net.infstudio.inspiringworld.magic.InspiringMagic;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author ci010
 */
@ModHandler
public class TestHandler {
    @SubscribeEvent
    public void method(PlayerInteractEvent.RightClickEmpty empty) {
        System.out.println("mod handler fine!");
        Item i = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation(InspiringMagic.MODID, "test"));
        if (i != null) {
            System.out.println("item exist!");
        }
        Block b = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(InspiringMagic.MODID, "test"));
        if (b != null) {
            System.out.println("block exist!");
        }
    }
}
