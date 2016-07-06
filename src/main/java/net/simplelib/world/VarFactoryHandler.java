package net.simplelib.world;

import api.simplelib.sync.Attributes;
import api.simplelib.capabilities.Capabilities;
import api.simplelib.capabilities.CapabilityBuilderHandler;
import api.simplelib.sync.VarSync;
import api.simplelib.sync.VarSyncFactory;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * @author ci010
 */
public class VarFactoryHandler implements CapabilityBuilderHandler<VarSyncFactory>
{
	@Override
	public VarSyncFactory createBuilder(Object contextSrc)
	{
		if (contextSrc instanceof Entity)
			return new EntityVarFactory((Entity) contextSrc);
		else
			return new CommonVarFactory();
	}

	@Override
	public void build(ImmutableMap.Builder<ResourceLocation, ICapabilityProvider> storage, VarSyncFactory varSyncFactory, Object context)
	{
		ImmutableList<VarSync> tracking = null;

		if (varSyncFactory instanceof EntityVarFactory)
			tracking = ((EntityVarFactory) varSyncFactory).getAllTracking();
		else if (varSyncFactory instanceof CommonVarFactory)
			tracking = ((CommonVarFactory) varSyncFactory).getAllTracking();

		if (tracking != null)
			storage.put(new ResourceLocation("sync"),
					Capabilities.newBuilder(Attributes.CAPABILITY).append(new Attributes((ICapabilityProvider)
							context, tracking)).build());
	}
}
