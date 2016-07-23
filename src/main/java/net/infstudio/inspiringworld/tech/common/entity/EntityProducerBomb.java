package net.infstudio.inspiringworld.tech.common.entity;

import net.infstudio.inspiringworld.tech.common.world.ProducerExplosion;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import java.util.List;

public class EntityProducerBomb extends EntityThrowable {
    public EntityProducerBomb(World worldIn) {
        super(worldIn);
    }

    public EntityProducerBomb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityProducerBomb(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.worldObj.isRemote) {
            EntityLivingBase entity = this.getThrower();
            if (entity != null) {
                String output = String.format("Boom! %f %f %f", this.posX, this.posY, this.posZ);
                entity.addChatMessage(new TextComponentString(output));
            }
            ProducerExplosion explosion =
                new ProducerExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, 5.0F);
            if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.worldObj, explosion)) {
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
            this.setDead();
        }
    }
}
