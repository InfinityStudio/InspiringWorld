package net.infstudio.inspiringworld.tech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author Blealtan
 */
public class BlockEnderLeaves extends Block {

    public BlockEnderLeaves() {
        super(Material.LEAVES);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return NULL_AABB;
    }
}
