package net.infstudio.inspiringworld.magic.item;

import net.infstudio.inspiringworld.magic.BlockPoses;
import net.infstudio.inspiringworld.magic.InspiringMagic;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ModComponent;
import net.infstudio.inspiringworld.magic.rune.RuneRuin;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * @author ci010
 */
@ModComponent
public class ItemEnergyCompass extends Item
{
    public ItemEnergyCompass()
    {
        this.addPropertyOverride(new ResourceLocation(InspiringMagic.MODID, "angle"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            double rotation;
            @SideOnly(Side.CLIENT)
            double rotaCache;
            @SideOnly(Side.CLIENT)
            long lastUpdateTick;

            @Override
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                    return 0;
                if (worldIn == null)
                    worldIn = entityIn.worldObj;
                BlockPos entityPos = entityIn.getPosition();
                RuneRuin closest = RuneRuin.getClosest(worldIn, entityPos);
                if (closest == null)
                    return 0;
                BlockPos position = closest.getPosition();

                return (float) BlockPoses.radiusOf(entityPos, position);
            }

            @SideOnly(Side.CLIENT)
            private double wobble(World world, double angle)
            {
                if (world.getTotalWorldTime() != this.lastUpdateTick)
                {
                    this.lastUpdateTick = world.getTotalWorldTime();
                    double mod = angle - this.rotation;
                    mod = mod % (Math.PI * 2D);
                    mod = MathHelper.clamp_double(mod, -1.0D, 1.0D);
                    this.rotaCache += mod * 0.1D;
                    this.rotaCache *= 0.8D;
                    this.rotation += this.rotaCache;
                }

                return this.rotation;
            }
        });
    }
}
