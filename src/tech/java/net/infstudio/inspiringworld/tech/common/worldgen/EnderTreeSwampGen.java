package net.infstudio.inspiringworld.tech.common.worldgen;

import java.lang.reflect.Field;
import java.util.Random;

import net.infstudio.inspiringworld.tech.common.block.IWTechBlocks;
import net.infstudio.inspiringworld.tech.common.config.IWTechConfig;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenSwamp;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author Blealtan
 */
class EnderTreeSwampGen extends WorldGenSwamp {

    private static final IBlockState TRUNK =
        Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
    private static final IBlockState NORMAL_LEAF =
        Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)
        .withProperty(BlockOldLeaf.CHECK_DECAY, false);
    private static final IBlockState ENDER_LEAF =
        IWTechBlocks.BLOCK_ENDER_LEAVES.getDefaultState().withProperty(BlockOldLeaf.CHECK_DECAY, false);

    EnderTreeSwampGen() {
        super();
    }

    @Override
    public boolean generate(World worldIn, Random rand, BlockPos position) {

        final boolean ender = rand.nextInt(5) == 0;

        int i;

        for (i = rand.nextInt(4) + 5;
            worldIn.getBlockState(position.down()).getMaterial() == Material.WATER;
            position = position.down()) {
            ;
        }

        boolean flag = true;

        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
                int k = 1;

                if (j == position.getY()) {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2) {
                    k = 3;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l) {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1) {
                        if (j >= 0 && j < 256) {
                            IBlockState iblockstate = worldIn.getBlockState(blockpos$mutableblockpos.setPos(l, j, i1));
                            Block block = iblockstate.getBlock();

                            if (!iblockstate.getBlock().isAir(iblockstate, worldIn, blockpos$mutableblockpos.setPos(l, j, i1)) && !iblockstate.getBlock().isLeaves(iblockstate, worldIn, blockpos$mutableblockpos.setPos(l, j, i1))) {
                                if (block != Blocks.WATER && block != Blocks.FLOWING_WATER) {
                                    flag = false;
                                } else if (j > position.getY()) {
                                    flag = false;
                                }
                            }
                        } else {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag) {
                return false;
            } else {
                BlockPos down = position.down();
                IBlockState state = worldIn.getBlockState(down);
                boolean isSoil = state.getBlock().canSustainPlant(state, worldIn, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling) Blocks.SAPLING);

                if (isSoil && position.getY() < worldIn.getHeight() - i - 1) {
                    state.getBlock().onPlantGrow(state, worldIn, position.down(), position);

                    for (int k1 = position.getY() - 3 + i; k1 <= position.getY() + i; ++k1) {
                        int j2 = k1 - (position.getY() + i);
                        int l2 = 2 - j2 / 2;

                        for (int j3 = position.getX() - l2; j3 <= position.getX() + l2; ++j3) {
                            int k3 = j3 - position.getX();

                            for (int i4 = position.getZ() - l2; i4 <= position.getZ() + l2; ++i4) {
                                int j1 = i4 - position.getZ();

                                if (Math.abs(k3) != l2 || Math.abs(j1) != l2 || rand.nextInt(2) != 0 && j2 != 0) {
                                    BlockPos blockpos = new BlockPos(j3, k1, i4);
                                    state = worldIn.getBlockState(blockpos);

                                    if (state.getBlock().canBeReplacedByLeaves(state, worldIn, blockpos)) {
                                        this.setBlockAndNotifyAdequately(worldIn, blockpos,
                                            ender ? ENDER_LEAF : NORMAL_LEAF);
                                    }
                                }
                            }
                        }
                    }

                    for (int l1 = 0; l1 < i; ++l1) {
                        BlockPos upN = position.up(l1);
                        IBlockState iblockstate1 = worldIn.getBlockState(upN);
                        Block block2 = iblockstate1.getBlock();

                        if (block2.isAir(iblockstate1, worldIn, upN) || block2.isLeaves(iblockstate1, worldIn, upN) || block2 == Blocks.FLOWING_WATER || block2 == Blocks.WATER) {
                            this.setBlockAndNotifyAdequately(worldIn, position.up(l1), EnderTreeSwampGen.TRUNK);
                        }
                    }

                    for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2) {
                        int k2 = i2 - (position.getY() + i);
                        int i3 = 2 - k2 / 2;
                        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                        for (int l3 = position.getX() - i3; l3 <= position.getX() + i3; ++l3) {
                            for (int j4 = position.getZ() - i3; j4 <= position.getZ() + i3; ++j4) {
                                blockpos$mutableblockpos1.setPos(l3, i2, j4);

                                if (worldIn.getBlockState(blockpos$mutableblockpos1).getMaterial() == Material.LEAVES) {
                                    BlockPos blockpos3 = blockpos$mutableblockpos1.west();
                                    BlockPos blockpos4 = blockpos$mutableblockpos1.east();
                                    BlockPos blockpos1 = blockpos$mutableblockpos1.north();
                                    BlockPos blockpos2 = blockpos$mutableblockpos1.south();

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos3)) {
                                        this.addVine(worldIn, blockpos3, BlockVine.EAST, ender);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos4)) {
                                        this.addVine(worldIn, blockpos4, BlockVine.WEST, ender);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos1)) {
                                        this.addVine(worldIn, blockpos1, BlockVine.SOUTH, ender);
                                    }

                                    if (rand.nextInt(4) == 0 && this.isAir(worldIn, blockpos2)) {
                                        this.addVine(worldIn, blockpos2, BlockVine.NORTH, ender);
                                    }
                                }
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void addVine(World worldIn, BlockPos pos, PropertyBool prop, boolean ender) {
        IBlockState iblockstate = ender ? IWTechBlocks.BLOCK_ENDER_WINE.getDefaultState().withProperty(prop, true) :
            Blocks.VINE.getDefaultState().withProperty(prop, true);
        this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
        int i = 4;

        for (pos = pos.down(); this.isAir(worldIn, pos) && i > 0; --i) {
            this.setBlockAndNotifyAdequately(worldIn, pos, iblockstate);
            pos = pos.down();
        }
    }

    private boolean isAir(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock().isAir(state, world, pos);
    }

    private static boolean appliedToVanilla = false;

    void applyToVanilla() {
        if (EnderTreeSwampGen.appliedToVanilla) {
            return;
        }
        EnderTreeSwampGen.appliedToVanilla = true;
        try {
            Field fieldSwamp = ReflectionHelper.findField(Biome.class, "SWAMP_FEATURE", "field_76763_Q");
            EnumHelper.setFailsafeFieldValue(fieldSwamp, null, this);
        } catch (Exception e) {
            IWTechConfig.logger().warn("InspiringWorld EnderTree generator's reflection failed:", e);
        }
    }
}
