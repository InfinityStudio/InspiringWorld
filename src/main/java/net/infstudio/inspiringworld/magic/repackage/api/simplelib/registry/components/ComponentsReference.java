package net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.FreeConstruct;

/**
 * <p>ALL the public static "available" fields in the class annotated by this will be registered.
 * A "available field" means it is an instance of:
 * <ul>
 * <li>{@link net.minecraft.block.Block}
 * <li>{@link net.minecraft.item.Item}
 * </ul>
 * Also, the field with the type annotated by {@link ComponentStruct} will be also treat as "available".
 * <p/>
 * <p>If the field is null and annotation {@link Construct} is presented, it will follow the {@link Construct} to
 * create a new instance to register.</p>
 * <p>If the field is null without annotation {@link Construct}, mod will try to find an instance by
 * {@link net.minecraftforge.fml.common.registry.GameRegistry#findBlock(String, String)} or
 * {@link net.minecraftforge.fml.common.registry.GameRegistry#findItem(String, String)}.</p>
 * <p>This should looks like the {@link net.minecraft.init.Blocks} and {@link net.minecraft.init.Items}</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value =
		{ElementType.TYPE})
@FreeConstruct
public @interface ComponentsReference
{
	/**
	 * @return The default domain of this class.
	 */
	String domain() default "minecraft";

	/**
	 * An optional @interface for component reference.
	 * <p>If there are </p>
	 * Inject a Block/Item/ComponentStruct reference by finding the include name.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(value =
			{ElementType.FIELD})
	@interface Ref
	{
		/**
		 * @return The register name of the component. The default format should be like
		 * {@link net.minecraft.util.ResourceLocation}: domain:name. The domain is the mod id of the component.
		 * <p>If it
		 * owned by Minecraft, The domain should be minecraft.</p>
		 * <p>If you simply put the register name here. The domain will be the the {@link ComponentsReference#domain()}</p>
		 */
		String value();
	}
}
