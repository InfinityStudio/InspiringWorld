package net.simplelib.world;

import api.simplelib.world.entity.LivingAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Vector3d;

/**
 * @author ci010
 */
public class LivingAIImpl implements LivingAbility
{
	private EntityLiving living;

	public LivingAIImpl(EntityLiving living)
	{
		this.living = living;
	}

	@Override
	public Sense getSense()
	{
		return new Sense()
		{
			@Override
			public boolean canSee(Entity entityIn)
			{
				return living.getEntitySenses().canSee(entityIn);
			}

			@Override
			public void setRevengeTarget(EntityLivingBase livingBase)
			{
				living.setRevengeTarget(livingBase);
			}

			@Override
			public EntityLivingBase getLastAttacker()
			{
				return living.getLastAttacker();
			}

			@Override
			public int getLastAttackerTime()
			{
				return living.getLastAttackerTime();
			}

			@Override
			public void setLastAttacker(EntityLivingBase entityIn)
			{
				living.setLastAttacker(entityIn);
			}

			@Override
			public void clearSense()
			{
				setLastAttacker(null);
				setRevengeTarget(null);
				living.getEntitySenses().clearSensingCache();
			}
		};
	}

	@Override
	public Body getBody()
	{
		return new Body()
		{
			@Override
			public boolean moveTo(Vector3d pos, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				return navigator.noPath() && navigator.tryMoveToXYZ(pos.x, pos.getY(), pos.getZ(), speed);
			}

			@Override
			public boolean moveTo(BlockPos pos, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				return navigator.noPath() && navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speed);
			}

			@Override
			public boolean moveTo(Entity entity, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				return navigator.noPath() && navigator.tryMoveToEntityLiving(entity, speed);
			}

			@Override
			public boolean forceMoveTo(Vector3d pos, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				navigator.clearPathEntity();
				return navigator.tryMoveToXYZ(pos.x, pos.getY(), pos.getZ(), speed);
			}

			@Override
			public boolean forceMoveTo(BlockPos pos, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				navigator.clearPathEntity();
				return navigator.tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), speed);
			}

			@Override
			public boolean forceMoveTo(Entity entity, double speed)
			{
				PathNavigate navigator = living.getNavigator();
				navigator.clearPathEntity();
				return navigator.tryMoveToEntityLiving(entity, speed);
			}

			@Override
			public double getMovingSpeed()
			{
				return living.getMoveHelper().getSpeed();
			}

			@Override
			public void lookAt(Entity entityIn, float deltaYaw, float deltaPitch)
			{
				living.getLookHelper().setLookPositionWithEntity(entityIn, deltaYaw, deltaPitch);
			}

			@Override
			public void look(Vector3d pos, float deltaYaw, float deltaPitch)
			{
				living.getLookHelper().setLookPosition(pos.getX(), pos.getY(), pos.getZ(), deltaYaw, deltaPitch);
			}

			@Override
			public Vector3d getLookPos()
			{
				return new Vector3d(living.getLookHelper().getLookPosX(),
						living.getLookHelper().getLookPosY(),
						living.getLookHelper().getLookPosZ());
			}
		};
	}

	@Override
	public AITasks passiveTasks()
	{
		return new AIWrap(living.tasks);
	}

	@Override
	public AITasks activeTasks()
	{
		return new AIWrap(living.targetTasks);
	}

	private class AIWrap implements AITasks
	{
		EntityAITasks tasks;

		public AIWrap(EntityAITasks tasks)
		{
			this.tasks = tasks;
		}

		@Override
		public void addAI(int priority, EntityAIBase ai)
		{
			tasks.addTask(priority, ai);
		}

		@Override
		public void removeAI(EntityAIBase task)
		{
			tasks.removeTask(task);
		}

		@Override
		public void removeAI(Class<? extends EntityAIBase> clz)
		{
			for (EntityAITasks.EntityAITaskEntry entry : tasks.taskEntries)
				if (entry.action.getClass() == clz)
					tasks.removeTask(entry.action);
		}
	}
}
