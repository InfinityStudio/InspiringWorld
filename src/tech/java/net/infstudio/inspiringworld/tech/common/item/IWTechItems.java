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
    public static final ArmorMaterial CONSUMER = ItemAbyssArmor.material;

    public static final Item consumerHelmet = new ItemConsumerArmor.Helmet();
    public static final Item consumerChestplate = new ItemConsumerArmor.Chestplate();
    public static final Item consumerLeggings = new ItemConsumerArmor.Leggings();
    public static final Item consumerBoots = new ItemConsumerArmor.Boots();

    public static final Item controller = new ItemController();
    public static final Item producerBomb = new ItemSourceBomb();

    public static void preInit() {
        IWTechItems.registerItem(IWTechItems.consumerHelmet.setRegistryName("abyss_helmet"));
        IWTechItems.registerItem(IWTechItems.consumerChestplate.setRegistryName("abyss_chestplate"));
        IWTechItems.registerItem(IWTechItems.consumerLeggings.setRegistryName("abyss_leggings"));
        IWTechItems.registerItem(IWTechItems.consumerBoots.setRegistryName("abyss_boots"));

        IWTechItems.registerItem(IWTechItems.controller.setRegistryName("controller"));
        IWTechItems.registerItem(IWTechItems.producerBomb.setRegistryName("source_bomb"));
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }
}
