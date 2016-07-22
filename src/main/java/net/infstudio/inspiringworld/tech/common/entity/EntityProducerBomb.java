package net.infstudio.inspiringworld.tech.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
            // TODO Explosion logic
            this.setDead();
        }
    }
}
