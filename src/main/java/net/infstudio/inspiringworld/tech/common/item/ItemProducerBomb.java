/**
 * InspiringWorld Mod for Minecraft.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.infstudio.inspiringworld.tech.common.item;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.entity.EntityProducerBomb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemProducerBomb extends Item {

    public ItemProducerBomb()
    {
        super();
        this.setUnlocalizedName(InspiringTech.MODID + "." + "producerBomb");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand) {
        if (!player.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        double x = player.posX, y = player.posY, z = player.posZ;
        float v = 0.5f, p = 1.0F / (Item.itemRand.nextFloat() + 2);
        world.playSound(null, x, y, z, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, v, p);
        if (!world.isRemote) {
            EntityProducerBomb bomb = new EntityProducerBomb(world, player);
            bomb.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.0F, 1.0F);
            world.spawnEntityInWorld(bomb);
        }
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
    }

    @SubscribeEvent
    public void onLootingLevel(LootingLevelEvent event) {
        if (event.getDamageSource() instanceof EntityDamageSource) {
            EntityDamageSource source = (EntityDamageSource) event.getDamageSource();
            if (source.getEntity() instanceof EntityProducerBomb) {
                event.setLootingLevel(1);
            }
        }
    }
}
