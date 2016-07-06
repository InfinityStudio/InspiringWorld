package api.simplelib.world;

import api.simplelib.world.chunk.ChunkData;
import api.simplelib.world.entity.EntityState;
import api.simplelib.registry.ModProxy;

import api.simplelib.world.entity.LivingAbility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public interface WorldPropertiesManager
{
	@ModProxy.Inject(genericType = WorldPropertiesManager.class)
	WorldPropertiesManager INSTANCE = null;

	ICapabilityProvider getCapabilityProvider(int dimension);

	ICapabilityProvider getCapabilityProvider(World world);

	ChunkData getChunkData(World world, BlockPos pos);

	ChunkData getChunkData(World world, ChunkPos pos);

	ChunkData getChunkData(Chunk chunk);

	EntityState getEntityState(Entity entity);

	LivingAbility getAi(EntityLiving living);
}
