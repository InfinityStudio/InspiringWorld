package api.simplelib.world.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * @author ci010
 */
public interface EntityState
{
	Entity getEntity();

	BlockPos getPos();

	World getWorld();

	int entityId();

	void destroy();

	void setCustomNameTag(String name);

	String getCustomNameTag();

	boolean hasCustomName();

	boolean isInWater();

	boolean isOnFire();

	boolean isAlive();

	boolean isInLava();

	boolean isImmuneToFire();

	boolean isInvisible();

	UUID entityUniqueID();
}
