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

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechItems {
    public static ArmorMaterial consumerArmorMaterial = ItemConsumerArmor.material;

    public static Item consumerHelmet = new ItemConsumerArmor.Helmet().setRegistryName("consumer_helmet");
    public static Item consumerChestplate = new ItemConsumerArmor.Chestplate().setRegistryName("consumer_chestplate");
    public static Item consumerLeggings = new ItemConsumerArmor.Leggings().setRegistryName("consumer_leggings");
    public static Item consumerBoots = new ItemConsumerArmor.Boots().setRegistryName("consumer_boots");

    public static Item controller = new ItemController().setRegistryName("controller");
    public static Item producerBomb = new ItemProducerBomb().setRegistryName("producer_bomb");

    public static void preInit() {
        IWTechItems.registerItem(IWTechItems.consumerHelmet);
        IWTechItems.registerItem(IWTechItems.consumerChestplate);
        IWTechItems.registerItem(IWTechItems.consumerLeggings);
        IWTechItems.registerItem(IWTechItems.consumerBoots);

        IWTechItems.registerItem(IWTechItems.controller);
        IWTechItems.registerItem(IWTechItems.producerBomb);
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }
}
