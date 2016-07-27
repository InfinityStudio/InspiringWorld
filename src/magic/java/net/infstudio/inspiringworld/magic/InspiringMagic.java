/**
 * InspiringWorld Mod for Minecraft.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.infstudio.inspiringworld.magic;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.Random;

@Mod(modid = InspiringMagic.MODID, version = InspiringMagic.VERSION, acceptedMinecraftVersions = InspiringMagic.MC_VERSION, dependencies = InspiringMagic.DEPENDENCIES)
public class InspiringMagic {
    public static final String MODID = "inspiringmagic";
    public static final String VERSION = "@version@";
    public static final String MC_VERSION = "1.10.2";
    public static final String DEPENDENCIES = "required-after:inspiringworld";

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }

    @SubscribeEvent//TODO register this after finish
    public void handleDropping(LivingDropsEvent event) {
        event.getEntity().worldObj.rand.nextDouble();//the possibility
        //TODO drop the item
    }

    private void addVillagerTrade() {
        VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation("priest")).getCareer(2).addTrade(2,
            new EntityVillager.ITradeList() {
                @Override
                public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random) {
                    //TODO add trading item.
                }
            });
    }

}
