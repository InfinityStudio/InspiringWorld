package net.infstudio.inspiringworld.magic.repackage.api.simplelib.utils;

import com.google.common.base.Predicate;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * @author ci010
 */
public class ScanAirTask extends ScanBlockTask
{
	public ScanAirTask(BlockPos pos, IBlockAccess access, int threadHold)
	{
		super(pos, access, threadHold, new Predicate<IBlockState>()
		{
			@Override
			public boolean apply(@Nullable IBlockState input)
			{
				return input.getBlock().getMaterial(input) == Material.AIR;
			}
		});
	}

	public ScanAirTask(BlockPos pos, IBlockAccess access, Set<BlockPos> set, int threadHold)
	{
		super(pos, access, set, threadHold, new Predicate<IBlockState>()
		{
			@Override
			public boolean apply(@Nullable IBlockState input)
			{
				return input.getBlock().getMaterial(input) == Material.AIR;
			}
		});
	}
}
