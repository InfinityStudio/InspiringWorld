package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntityAntenna;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockAntenna extends BlockContainer {

    public BlockAntenna() {
        this("antenna");
    }

    public BlockAntenna(String unlocalizedName) {
        super(Material.IRON);
        this.setUnlocalizedName(InspiringTech.MODID + "." + unlocalizedName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(IWTechBlocks.FACING, EnumFacing.UP));
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        // TODO: must at correct machines
        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
        int meta, EntityLivingBase placer) {
        IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        return origin.withProperty(IWTechBlocks.FACING, facing);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAntenna();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(IWTechBlocks.FACING, EnumFacing.values()[(meta & 7) % 6]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IWTechBlocks.FACING).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        // Do not change it to var args
        // Maybe it will be replaced to extended blockstates
        return new BlockStateContainer(this, new IProperty<?>[] { IWTechBlocks.FACING });
    }
}
