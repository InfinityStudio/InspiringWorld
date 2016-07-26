package api.simplelib.capabilities;

import java.lang.annotation.*;

/**
 * This annotation will means the method annotated by this will provide a
 * {@link net.minecraftforge.common.capabilities.Capability}.
 * <p>
 * The form should be:
 * <p>@CapabilityBuild
 * <p>{@code public void buildSomething(CapabilityBuilder context){...}}</p>
 * <p>The name of the method is free. Can be anything.
 * <p>The object containing the methods annotated by this will be able to handle by
 * <p>{@link api.simplelib.capabilities.Capabilities#revolve(net.minecraft.entity.Entity, Object)},
 * <p>{@link api.simplelib.capabilities.Capabilities#revolve(net.minecraft.tileentity.TileEntity, Object)},
 * <p>{@link api.simplelib.capabilities.Capabilities#revolve(net.minecraft.item.ItemStack, Object)}</p>
 *
 * @see CapabilityBuilder
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface CapabilityBuild
{
}
