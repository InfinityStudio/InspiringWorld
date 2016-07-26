package api.simplelib.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Vector3d;

/**
 * @author ci010
 */
public interface LivingAbility
{
	Sense getSense();

	Body getBody();

	AITasks passiveTasks();

	AITasks activeTasks();

	interface AITasks
	{
		void addAI(int priority, EntityAIBase ai);

		void removeAI(EntityAIBase task);

		void removeAI(Class<? extends EntityAIBase> clz);
	}

	interface Body
	{
		boolean moveTo(Vector3d pos, double speed);

		boolean moveTo(BlockPos pos, double speed);

		boolean moveTo(Entity entity, double speed);

		boolean forceMoveTo(Vector3d pos, double speed);

		boolean forceMoveTo(BlockPos pos, double speed);

		boolean forceMoveTo(Entity entity, double speed);

		double getMovingSpeed();

		void lookAt(Entity entityIn, float deltaYaw, float deltaPitch);

		void look(Vector3d pos, float deltaYaw, float deltaPitch);

		Vector3d getLookPos();
	}

	interface Sense
	{
		boolean canSee(Entity entityIn);

		void setRevengeTarget(EntityLivingBase livingBase);

		EntityLivingBase getLastAttacker();

		int getLastAttackerTime();

		void setLastAttacker(EntityLivingBase entityIn);

		void clearSense();
	}
}
