package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceAntenna;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSourceAntenna extends BlockAntenna {
    public BlockSourceAntenna() {
        super("antenna.source");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySourceAntenna();
    }
}
