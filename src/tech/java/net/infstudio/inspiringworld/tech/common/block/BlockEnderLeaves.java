package net.infstudio.inspiringworld.tech.common.block;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * @author Blealtan
 */
public class BlockEnderLeaves extends BlockLeaves {

    public BlockEnderLeaves() {
        super();
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this, 1, 0));
    }

    @Override
    protected int getSaplingDropChance(IBlockState state)
    {
        // The larger this number, the less the chance. Currently the same as jungle saplings.
        return 40;
    }
}
