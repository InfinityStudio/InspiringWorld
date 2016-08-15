package net.infstudio.inspiringworld.tech.common.block;

import java.util.Random;

import net.infstudio.inspiringworld.tech.common.worldgen.EnderTreeNormalGen;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * @author Blealtan
 */
public class BlockEnderSapling extends BlockSapling {
    public BlockEnderSapling() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        WorldGenerator worldgenerator = new EnderTreeNormalGen(true);
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
        if (!worldgenerator.generate(worldIn, rand, pos))
            worldIn.setBlockState(pos, state, 4);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(STAGE, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(STAGE);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, TYPE, STAGE);
    }
}
