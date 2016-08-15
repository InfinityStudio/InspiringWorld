package net.infstudio.inspiringworld.tech.common.block;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Blealtan
 */
public class BlockEnderLeaves extends BlockLeaves {

    public BlockEnderLeaves() {
        super();
        this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true).withProperty(CHECK_DECAY, true));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) != 0).withProperty(CHECK_DECAY, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(DECAYABLE)) i |= 1;
        if (state.getValue(CHECK_DECAY)) i |= 2;
        return i;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this, 1, 0));
    }

    @Override
    protected int getSaplingDropChance(IBlockState state) {
        // The larger this number, the less the chance. Currently the same as jungle saplings.
        return 40;
    }
}
