package net.infstudio.inspiringworld.tech.common.item;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ItemConsumerArmor extends ItemArmor {
    static ArmorMaterial material = EnumHelper.addArmorMaterial("CONSUMER", InspiringTech.MODID + ":" + "consumer", 10,
        new int[] { 1, 3, 4, 2 }, 15, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0.0F);

    protected ItemConsumerArmor(EntityEquipmentSlot equipmentSlotIn) {
        super(ItemConsumerArmor.material, ItemConsumerArmor.material.ordinal(), equipmentSlotIn);
    }

    public static class Helmet extends ItemConsumerArmor {
        public Helmet() {
            super(EntityEquipmentSlot.HEAD);
            this.setUnlocalizedName("consumerHelmet");
        }
    }

    public static class Chestplate extends ItemConsumerArmor {
        public Chestplate() {
            super(EntityEquipmentSlot.CHEST);
            this.setUnlocalizedName("consumerChestplate");
        }
    }

    public static class Leggings extends ItemConsumerArmor {
        public Leggings() {
            super(EntityEquipmentSlot.LEGS);
            this.setUnlocalizedName("consumerLeggings");
        }
    }

    public static class Boots extends ItemConsumerArmor {
        public Boots() {
            super(EntityEquipmentSlot.FEET);
            this.setUnlocalizedName("consumerBoots");
        }
    }
}
