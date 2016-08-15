package net.infstudio.inspiringworld.tech.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

/**
 * @author Blealtan
 */
public class EntityEnderSpider extends EntitySpider {
    public EntityEnderSpider(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 0.5F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (super.attackEntityAsMob(entityIn)) {

            // Handle knock back
            if (entityIn instanceof EntityLivingBase) {
                int i = 0;

                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
                    i = 1;
                } else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
                    i = 2;
                }

                if (i > 0) {
                    ((EntityLivingBase) entityIn).knockBack(this, i,
                        (double) MathHelper.sin(this.rotationYaw * 0.017453292F),
                        (double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                }
            }

            // Take the exp
            if (entityIn instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entityIn;

                final int exp = 10; // TODO: Put this in configuration?

                if (player.experienceTotal < exp) {
                    player.experienceLevel = 0;
                    player.experience = 0.0F;
                    player.experienceTotal = 0;
                } else {
                    player.experience -= exp / (float) player.xpBarCap();
                    for (player.experienceTotal -= exp;
                         player.experience <= 0.0F;
                         player.experience /= (float) player.xpBarCap()) {
                        player.experience = (player.experience + 1.0F) * (float) player.xpBarCap();
                        player.removeExperienceLevel(1);
                    }
                }
            }
            return true;
        }
        return false;
    }
}
