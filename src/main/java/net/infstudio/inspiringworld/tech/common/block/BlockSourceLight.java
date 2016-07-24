package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPurpurSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Blealtan
 */
public class BlockSourceLight extends BlockContainer {

    public BlockSourceLight() {
        super(IWTechBlocks.SOURCE_BLOCKS);
        this.setLightLevel(1.0F);
        MinecraftForge.EVENT_BUS.register(this);
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

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        double distanceMin = Double.MAX_VALUE;

        for (int i = -31; i <= 32; ++i)
            for (int j = -31; j <= 32; ++j)
                for (int k = -31; k <= 32; ++k)
                    if (event.getWorld().getTileEntity(
                        new BlockPos(event.getX() + i, event.getY() + j, event.getZ() + k))
                        instanceof TileEntitySourceLight)
                        distanceMin = Double.min(distanceMin, MathHelper.sqrt_double(i * i + j * j + k * k));

        if (event.getWorld().rand.nextDouble() * 32.0D > distanceMin)
            event.setResult(Event.Result.DENY);
    }
}
