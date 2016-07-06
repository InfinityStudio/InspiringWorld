package api.simplelib.capabilities;


import api.simplelib.registry.FreeConstruct;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

/**
 * The handle which creates the context and handles it to capability instance.
 * Use {@link ModCapabilityBuilderHandler} to register.
 *
 * @author ci010
 */
@FreeConstruct
public interface CapabilityBuilderHandler<Builder>
{
	/**
	 * Create builder and provide this to user to buildUnsaved. We just collect the information after they use this builder.
	 *
	 * @param contextSrc The src could be {@link net.minecraft.entity.Entity}, {@link net.minecraft.item.ItemStack}
	 *                   and {@link net.minecraft.tileentity.TileEntity}.
	 * @return The context. If you don't want to support to handle this context, just return null.
	 */
	Builder createBuilder(Object contextSrc);

	/**
	 * Create the capability instance by the builder after user's operation.
	 *
	 * @param output  The capability output. Just put all the result into this.
	 * @param builder The capability builder after operation.
	 * @param context The capability context.
	 */
	void build(ImmutableMap.Builder<ResourceLocation, ICapabilityProvider> output, Builder builder, Object context);
}
