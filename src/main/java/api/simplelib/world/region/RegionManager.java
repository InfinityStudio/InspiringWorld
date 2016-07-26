package api.simplelib.world.region;

import com.google.common.base.Optional;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

/**
 * @author ci010
 */
public interface RegionManager
{
	@CapabilityInject(RegionManager.class)
	Capability<RegionManager> REGION_MANAGER = null;

	World getWorld();

	Optional<Region> getRegion(int x, int z);

	Optional<Region> getRegion(BlockPos pos);

	Optional<Region> newRegion(String id, int x1, int z1, int x2, int z2);

	void deleteRegion(Region region);
}
