package net.infstudio.inspiringworld.magic.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author ci010
 */
public class BlockOreQuenchingMachine extends BlockLit
{
    protected BlockOreQuenchingMachine()
    {
        super(Material.ROCK);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityImpl();
    }

    public static class TileEntityImpl extends TileEntity implements ITickable
    {
        @Override
        public void tick()
        {

        }
    }
}
