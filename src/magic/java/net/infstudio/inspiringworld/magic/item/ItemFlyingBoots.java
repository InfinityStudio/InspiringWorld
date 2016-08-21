package net.infstudio.inspiringworld.magic.item;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ModComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * @author ci010
 */
@ModComponent
public class ItemFlyingBoots extends ItemArmor
{
    public ItemFlyingBoots()
    {
        super(ArmorMaterial.LEATHER, 0, EntityEquipmentSlot.FEET);
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
    {
        super.onArmorTick(world, player, itemStack);
        if (!player.isPotionActive(MobEffects.JUMP_BOOST))
            player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, 40, 2));//event this handles the fall
        // damage, consider about the percentage reduce of fall damage.
    }
}
