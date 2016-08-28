package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntityIWAntenna;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockIWAntenna extends BlockContainer {

    protected BlockIWAntenna() {
        super(Material.IRON);
        this.setUnlocalizedName(InspiringTech.MODID + "." + "antenna");
        this.setDefaultState(this.blockState.getBaseState().withProperty(IWTechBlocks.FACING, EnumFacing.UP));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityIWAntenna();
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
