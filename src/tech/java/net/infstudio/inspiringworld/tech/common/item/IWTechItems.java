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
import net.minecraftforge.oredict.OreDictionary;

public class IWTechItems {
    public static final ArmorMaterial ABYSS = ItemAbyssArmor.material;

    public static final Item ABYSS_HELMET = new ItemAbyssArmor.Helmet();
    public static final Item ABYSS_CHESTPLATE = new ItemAbyssArmor.Chestplate();
    public static final Item ABYSS_LEGGINGS = new ItemAbyssArmor.Leggings();
    public static final Item ABYSS_BOOTS = new ItemAbyssArmor.Boots();

    public static final Item CONTROLLER = new ItemController();
    public static final Item SOURCE_BOMB = new ItemSourceBomb();

    public static final Item ENDER_INGOT = new Item().setUnlocalizedName(InspiringTech.MODID + "." + "ingotEnder");
    public static final Item ENDER_POWDER = new Item().setUnlocalizedName(InspiringTech.MODID + "." + "powderEnder");

    public static void preInit() {
        registerItem(ABYSS_HELMET.setRegistryName(InspiringTech.MODID, "abyss_helmet"));
        registerItem(ABYSS_CHESTPLATE.setRegistryName(InspiringTech.MODID, "abyss_chestplate"));
        registerItem(ABYSS_LEGGINGS.setRegistryName(InspiringTech.MODID, "abyss_leggings"));
        registerItem(ABYSS_BOOTS.setRegistryName(InspiringTech.MODID, "abyss_boots"));

        registerItem(CONTROLLER.setRegistryName(InspiringTech.MODID, "controller"));
        registerItem(SOURCE_BOMB.setRegistryName(InspiringTech.MODID, "source_bomb"));

        registerItem(ENDER_INGOT.setRegistryName(InspiringTech.MODID, "ender_ingot"));
        registerItem(ENDER_POWDER.setRegistryName(InspiringTech.MODID, "ender_powder"));

        OreDictionary.registerOre("ingotEnder", ENDER_INGOT);
        OreDictionary.registerOre("powderEnder", ENDER_POWDER);
    }

    public static void registerItem(Item item) {
        GameRegistry.register(item);
    }
}
