package net.infstudio.inspiringworld.tech.common.item;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ItemAbyssArmor extends ItemArmor {
    static ArmorMaterial material = EnumHelper.addArmorMaterial("ABYSS", InspiringTech.MODID + ":" + "abyss", 10,
        new int[] { 1, 3, 4, 2 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    private float reduction;

    protected ItemAbyssArmor(EntityEquipmentSlot equipmentSlotIn, float reduction) {
        super(ItemAbyssArmor.material, ItemAbyssArmor.material.ordinal(), equipmentSlotIn);
        this.reduction = reduction;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static class Helmet extends ItemAbyssArmor {
        public Helmet() {
            super(EntityEquipmentSlot.HEAD, 0.1f);
            this.setUnlocalizedName("abyssHelmet");
        }
    }

    public static class Chestplate extends ItemAbyssArmor {
        public Chestplate() {
            super(EntityEquipmentSlot.CHEST, 0.3f);
            this.setUnlocalizedName("abyssChestplate");
        }
    }

    public static class Leggings extends ItemAbyssArmor {
        public Leggings() {
            super(EntityEquipmentSlot.LEGS, 0.2f);
            this.setUnlocalizedName("abyssLeggings");
        }
    }

    public static class Boots extends ItemAbyssArmor {
        public Boots() {
            super(EntityEquipmentSlot.FEET, 0.1f);
            this.setUnlocalizedName("abyssBoots");
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!event.getEntity().worldObj.isRemote) {
            if (event.getEntity() instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) event.getEntity();
                // Exclude Creative Players
                if (player.isCreative()) {
                    return;
                }

                // Check Armor in Inventory
                for (ItemStack stack : player.getArmorInventoryList()) {
                    if (stack.getItem() == this) {
                        if (player.experienceTotal > event.getAmount()) {
                            float amount = event.getAmount();
                            // Reduce the attack amount
                            event.setAmount(amount * (1 - reduction));
                            // Take the experience
                            int exp = (int) (amount * reduction);
                            player.experience -= exp / (float) player.xpBarCap();
                            for (player.experienceTotal -= exp; player.experience <= 0.0F;
                                 player.experience /= (float) player.xpBarCap()) {
                                player.experience = (player.experience + 1.0F) * (float) player.xpBarCap();
                                player.removeExperienceLevel(1);
                            }
                        }
                        else {
                            // Reduce the attack amount
                            event.setAmount(event.getAmount() - player.experienceTotal);
                            // Take the experience
                            player.experienceLevel = 0;
                            player.experience = 0.0F;
                            player.experienceTotal = 0;
                        }
                        return;
                    }
                }
            }
        }
    }
}
