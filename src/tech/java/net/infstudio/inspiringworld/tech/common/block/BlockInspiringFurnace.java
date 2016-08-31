package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntityInspiringFurnace;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockInspiringFurnace extends BlockContainer {
    public BlockInspiringFurnace() {
        super(Material.IRON);
        this.setUnlocalizedName(InspiringTech.MODID + "." + "inspiringFurnace");
        this.setDefaultState(this.blockState.getBaseState().withProperty(IWTechBlocks.HORIZON, EnumFacing.NORTH));
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityInspiringFurnace();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        // Do not change it to var args
        // Maybe it will be replaced to extended blockstates
        return new BlockStateContainer(this, new IProperty<?>[] { IWTechBlocks.HORIZON });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState origin = this.getDefaultState();
        EnumFacing facing = EnumFacing.getHorizontal(meta).getOpposite();
        return origin.withProperty(IWTechBlocks.HORIZON, facing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(IWTechBlocks.HORIZON).getOpposite().getHorizontalIndex();
    }
}
