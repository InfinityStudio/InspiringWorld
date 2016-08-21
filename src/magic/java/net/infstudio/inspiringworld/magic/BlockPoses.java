package net.infstudio.inspiringworld.magic;

import net.minecraft.util.math.BlockPos;

/**
 * @author ci010
 */
public class BlockPoses
{
    public static double radiusOf(BlockPos pos, BlockPos target)
    {
        return Math.atan2(-pos.getZ() + target.getZ(), -pos.getX() + target.getX());
    }
}
