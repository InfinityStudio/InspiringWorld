package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;

/**
 * @author Blealtan
 */
public class BlockSourceLight extends BlockContainer {

    public BlockSourceLight() {
        super(IWTechBlocks.SOURCE_BLOCKS);
        this.setLightLevel(1.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySourceLight();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
