package net.simplelib;


import api.simplelib.utils.RecursiveTask;
import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.Set;


/**
 * @author ci010
 */
public class ScanBlockTask extends RecursiveTask<Set<BlockPos>>
{
	protected BlockPos pos;
	protected IBlockAccess access;
	protected Set<BlockPos> set;
	protected int threadHold;
	protected Predicate<IBlockState> predicate;

	public ScanBlockTask(BlockPos pos, IBlockAccess access, int threadHold, Predicate<IBlockState> predicate)
	{
		this(pos, access, Sets.<BlockPos>newConcurrentHashSet(), threadHold, predicate);
	}

	public ScanBlockTask(BlockPos pos, IBlockAccess access, Set<BlockPos> set, int threadHold, Predicate<IBlockState>
			predicate)
	{
		this.pos = pos;
		this.access = access;
		this.set = set;
		this.threadHold = threadHold;
		this.predicate = predicate;
	}

	protected ScanBlockTask newTask(BlockPos pos)
	{
		return new ScanBlockTask(pos, access, set, threadHold, predicate);
	}

	@Override
	protected Set<BlockPos> compute()
	{
		if (set.size() >= threadHold)
			return set;
		if (!set.contains(pos))
			if (predicate.apply(access.getBlockState(pos)))
			{
				set.add(pos);
				BlockPos pos;
				for (EnumFacing enumFacing : EnumFacing.values())
				{
					pos = this.pos.offset(enumFacing);
					if (!set.contains(pos))
						newTask(pos).fork();
				}
			}
		return set;
	}
}
