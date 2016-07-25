package net.infstudio.inspiringworld.tech.common.block;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.infstudio.inspiringworld.tech.common.tileentity.TileEntitySourceLight;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Blealtan
 * @author ustc_zzzz
 */
public class BlockSourceLight extends BlockContainer {

    public BlockSourceLight() {
        super(IWTechBlocks.SOURCE_BLOCKS);
        this.setLightLevel(1.0F);
        this.setUnlocalizedName(InspiringTech.MODID + "." + "producerLight");
        this.setDefaultState(this.blockState.getBaseState().withProperty(IWTechBlocks.FACING, EnumFacing.NORTH)
            .withProperty(IWTechBlocks.WORKING, true));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntitySourceLight();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                     int meta, EntityLivingBase placer) {
        IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        return origin.withProperty(IWTechBlocks.FACING, facing);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState origin = this.getDefaultState();
        EnumFacing facing = EnumFacing.values()[(meta & 7) % 6];
        return origin.withProperty(IWTechBlocks.FACING, facing).withProperty(IWTechBlocks.WORKING, (meta & 8) > 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(IWTechBlocks.FACING).ordinal();
        int working = state.getValue(IWTechBlocks.WORKING) ? 8 : 0;
        return facing | working;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        // Do not change it to var args
        // Maybe it will be replaced to extended blockstates
        return new BlockStateContainer(this, new IProperty<?>[]{IWTechBlocks.FACING, IWTechBlocks.WORKING});
    }

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        double distanceMin = Double.MAX_VALUE;

        for (int i = -31; i <= 32; ++i)
            for (int j = -31; j <= 32; ++j)
                for (int k = -31; k <= 32; ++k) {
                    BlockPos pos = new BlockPos(event.getX() + i, event.getY() + j, event.getZ() + k);
                    IBlockState state = event.getWorld().getBlockState(pos);
                    if (state.getBlock() instanceof BlockSourceLight && state.getValue(IWTechBlocks.WORKING))
                        distanceMin = Math.min(distanceMin, MathHelper.sqrt_double(i * i + j * j + k * k));
                }

        if (event.getWorld().rand.nextDouble() * 32.0D > distanceMin)
            event.setResult(Event.Result.DENY);
    }
}
