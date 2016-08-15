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
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IWTechItems {
    public static final ArmorMaterial ABYSS = ItemAbyssArmor.material;

    public static final Item abyssHelmet = new ItemAbyssArmor.Helmet();
    public static final Item abyssChestplate = new ItemAbyssArmor.Chestplate();
    public static final Item abyssLeggings = new ItemAbyssArmor.Leggings();
    public static final Item abyssBoots = new ItemAbyssArmor.Boots();

    public static final Item controller = new ItemController();
    public static final Item sourceBomb = new ItemSourceBomb();

    public static void preInit() {
        IWTechItems.registerItem(IWTechItems.abyssHelmet.setRegistryName(InspiringTech.MODID, "abyss_helmet"));
        IWTechItems.registerItem(IWTechItems.abyssChestplate.setRegistryName(InspiringTech.MODID, "abyss_chestplate"));
        IWTechItems.registerItem(IWTechItems.abyssLeggings.setRegistryName(InspiringTech.MODID, "abyss_leggings"));
        IWTechItems.registerItem(IWTechItems.abyssBoots.setRegistryName(InspiringTech.MODID, "abyss_boots"));

        IWTechItems.registerItem(IWTechItems.controller.setRegistryName(InspiringTech.MODID, "controller"));
        IWTechItems.registerItem(IWTechItems.sourceBomb.setRegistryName(InspiringTech.MODID, "source_bomb"));
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }
}
