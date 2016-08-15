package net.infstudio.inspiringworld.tech.common.block;

import java.util.Collections;
import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Blealtan
 */
public class BlockEnderLeaves extends BlockLeaves {

    public BlockEnderLeaves() {
        super();
        this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true).withProperty(CHECK_DECAY, true));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCheckSpawn(LivingSpawnEvent.CheckSpawn event) {
        // Check if there is nearby ender leaves.
        for (int i = -4; i <= 4; ++i)
            for (int j = -4; j <= 4; ++j)
                for (int k = -4; k <= 4; ++k)
                    if (event.getWorld().getBlockState(
                        new BlockPos(event.getX() + i, event.getY() + j, event.getZ() + k)).getBlock()
                        .equals(IWTechBlocks.blockEnderLeaves))
                        return;
        // Without nearby ender leaves, 50% chance to cancel it.
        if (event.getWorld().rand.nextBoolean()) {
            event.setResult(Event.Result.DENY);
        }
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DECAYABLE, (meta & 4) != 0).withProperty(CHECK_DECAY, (meta & 8) != 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        if (state.getValue(DECAYABLE)) i |= 1;
        if (state.getValue(CHECK_DECAY)) i |= 2;
        return i;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return BlockPlanks.EnumType.OAK;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Collections.singletonList(new ItemStack(this, 1, 0));
    }

    @Override
    protected int getSaplingDropChance(IBlockState state) {
        // The larger this number, the less the chance. Currently the same as jungle saplings.
        return 40;
    }
}
