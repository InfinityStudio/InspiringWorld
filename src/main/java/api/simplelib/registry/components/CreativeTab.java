package api.simplelib.registry.components;

import com.google.common.annotations.Beta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ci010
 */
@Beta
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CreativeTab
{
	/**
	 * @return The index of the creative tab.
	 * @see net.minecraft.creativetab.CreativeTabs#tabIndex
	 */
	int index();
}
