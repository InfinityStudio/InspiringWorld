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
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModCreativeTab
{
	/**
	 * THe id of creative tabs.
	 *
	 * @see net.minecraft.creativetab.CreativeTabs#tabIndex
	 */
	int BUILDING_BLOCKS = 0, DECORATIONS = 1, REDSTONE = 2, TRANSPORTATION = 3, MISC = 4, SEARCH = 5, FOOD = 6,
			TOOLS = 7, COMBAT = 8, BREWING = 9, MATERIALS = 10, INVENTORY = 11;

	/**
	 * @return The index of the creative tab.
	 * @see net.minecraft.creativetab.CreativeTabs#tabIndex
	 */
	int value();
}
