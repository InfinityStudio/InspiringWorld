package net.infstudio.inspiringworld.tech.common.world;

import com.google.common.collect.Maps;
import net.infstudio.inspiringworld.tech.common.entity.EntityProducerBomb;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

/**
 * @author Blealtan
 */
public class ProducerExplosion extends Explosion {

    private final World worldObj;
    private final double explosionX;
    private final double explosionY;
    private final double explosionZ;
    private final Entity exploder;
    private final float explosionSize;
    private final Map<EntityPlayer, Vec3d> playerKnockbackMap;

    public ProducerExplosion(World worldIn, EntityProducerBomb bomb, double x, double y, double z,
                             float size) {
        super(worldIn, bomb, x, y, z, size, false, true);
        this.playerKnockbackMap = Maps.<EntityPlayer, Vec3d>newHashMap();
        this.worldObj = worldIn;
        this.exploder = bomb;
        this.explosionSize = size;
        this.explosionX = x;
        this.explosionY = y;
        this.explosionZ = z;
    }

    /**
     * Does the first part of the explosion (only make damage to entities)
     */
    @Override
    public void doExplosionA() {
        float f3 = this.explosionSize * 2.0F;
        int k1 = MathHelper.floor_double(this.explosionX - (double) f3 - 1.0D);
        int l1 = MathHelper.floor_double(this.explosionX + (double) f3 + 1.0D);
        int i2 = MathHelper.floor_double(this.explosionY - (double) f3 - 1.0D);
        int i1 = MathHelper.floor_double(this.explosionY + (double) f3 + 1.0D);
        int j2 = MathHelper.floor_double(this.explosionZ - (double) f3 - 1.0D);
        int j1 = MathHelper.floor_double(this.explosionZ + (double) f3 + 1.0D);
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this.exploder,
            new AxisAlignedBB((double) k1, (double) i2, (double) j2, (double) l1, (double) i1, (double) j1));
        net.minecraftforge.event.ForgeEventFactory.onExplosionDetonate(this.worldObj, this, list, f3);
        Vec3d vec3d = new Vec3d(this.explosionX, this.explosionY, this.explosionZ);

        for (Entity entity : list) {

            if (!entity.isImmuneToExplosions()) {
                double d12 = entity.getDistance(this.explosionX, this.explosionY, this.explosionZ) / (double) f3;

                if (d12 <= 1.0D) {
                    double d5 = entity.posX - this.explosionX;
                    double d7 = entity.posY + (double) entity.getEyeHeight() - this.explosionY;
                    double d9 = entity.posZ - this.explosionZ;
                    double d13 = (double) MathHelper.sqrt_double(d5 * d5 + d7 * d7 + d9 * d9);

                    if (d13 != 0.0D) {
                        d5 = d5 / d13;
                        d7 = d7 / d13;
                        d9 = d9 / d13;
                        double d14 = (double) this.worldObj.getBlockDensity(vec3d, entity.getEntityBoundingBox());
                        double d10 = (1.0D - d12) * d14;
                        entity.attackEntityFrom(
                            new EntityDamageSource("explosion", exploder).setDifficultyScaled().setExplosion(),
                            (float) ((int) ((d10 * d10 + d10) / 2.0D * 7.0D * (double) f3 + 1.0D)));
                        double d11 = 1.0D;

                        if (entity instanceof EntityLivingBase) {
                            d11 = EnchantmentProtection.getBlastDamageReduction((EntityLivingBase) entity, d10);
                        }

                        entity.motionX += d5 * d11;
                        entity.motionY += d7 * d11;
                        entity.motionZ += d9 * d11;

                        if (entity instanceof EntityPlayer) {
                            EntityPlayer entityplayer = (EntityPlayer) entity;

                            if (!entityplayer.isSpectator() &&
                                (!entityplayer.isCreative() || !entityplayer.capabilities.isFlying)) {
                                this.playerKnockbackMap.put(entityplayer, new Vec3d(d5 * d10, d7 * d10, d9 * d10));
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Map<EntityPlayer, Vec3d> getPlayerKnockbackMap()
    {
        return this.playerKnockbackMap;
    }
}
