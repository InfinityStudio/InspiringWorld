package net.infstudio.inspiringworld.tech.common.block;

import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Blealtan
 */
public class BlockEnderVine extends BlockVine {
    public BlockEnderVine() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();
        if (entity.worldObj.isRemote)
            return;
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isSpectator())
            return;

        int i = MathHelper.floor_double(entity.posX);
        int j = MathHelper.floor_double(entity.getEntityBoundingBox().minY);
        int k = MathHelper.floor_double(entity.posZ);
        IBlockState state = entity.worldObj.getBlockState(new BlockPos(i, j, k));

        if (ForgeModContainer.fullBoundingBoxLadders) {
            AxisAlignedBB bb = entity.getEntityBoundingBox();
            int mX = MathHelper.floor_double(bb.minX);
            int mY = MathHelper.floor_double(bb.minY);
            int mZ = MathHelper.floor_double(bb.minZ);
            for (int y2 = mY; y2 < bb.maxY; y2++) {
                for (int x2 = mX; x2 < bb.maxX; x2++) {
                    for (int z2 = mZ; z2 < bb.maxZ; z2++) {
                        BlockPos tmp = new BlockPos(x2, y2, z2);
                        state = entity.worldObj.getBlockState(tmp);
                    }
                }
            }
        }

        if (!state.getBlock().equals(this))
            return;

        entity.motionY *= 2;
    }
}
