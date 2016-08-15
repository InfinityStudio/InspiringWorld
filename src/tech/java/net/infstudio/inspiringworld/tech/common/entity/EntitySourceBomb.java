package net.infstudio.inspiringworld.tech.common.entity;

import net.infstudio.inspiringworld.tech.common.world.SourceBombExplosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class EntitySourceBomb extends EntityThrowable {
    public EntitySourceBomb(World worldIn) {
        super(worldIn);
    }

    public EntitySourceBomb(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntitySourceBomb(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        if (!this.worldObj.isRemote) {
            SourceBombExplosion explosion =
                new SourceBombExplosion(this.worldObj, this, this.posX, this.posY, this.posZ, 5.0F);
            if (!net.minecraftforge.event.ForgeEventFactory.onExplosionStart(this.worldObj, explosion)) {
                explosion.doExplosionA();
                explosion.doExplosionB(true);
            }
            this.setDead();
        }
    }
}
