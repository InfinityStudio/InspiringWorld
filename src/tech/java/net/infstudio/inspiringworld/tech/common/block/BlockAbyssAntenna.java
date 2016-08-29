package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.common.tileentity.TileEntityAbyssAntenna;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAbyssAntenna extends BlockAntenna {
    public BlockAbyssAntenna() {
        super("antenna.abyss");
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityAbyssAntenna();
    }
}
