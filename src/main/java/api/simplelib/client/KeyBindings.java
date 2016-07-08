package api.simplelib.client;

import com.google.common.base.Joiner;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import org.apache.commons.lang3.ArrayUtils;

/**
 * @author ci010
 */
public class KeyBindings
{
	/**
	 * The existed category of the Minecraft {@link KeyBinding}.
	 */
	public static final String MOVEMENT = "key.categories.movement", GAMEPLAY = "key.categories.gameplay",
			INVENTORY = "key.categories.inventory", MULTIPLAYER = "key.categories.multiplayer",
			MISC = "key.categories.misc";

	/**
	 * @param id       The id of the {@link KeyBinding}. Could be simple like "hit" or "toggle". Also key be default style,
	 *                 "key.xxx".
	 * @param keyCode  The Key code, see {@link org.lwjgl.input.Keyboard}.
	 * @param category The category of the {@link KeyBinding}.
	 * @return A new keyBinding
	 */
	public static KeyBinding create(String id, int keyCode, String category)
	{
		if (!id.startsWith("key."))
			id = "key.".concat(id);
		if (!category.startsWith("key.categories."))
		{
			String[] arr = category.split(".");
			if (arr[0].equals(category))
				category = "key.categories.".concat(category);
			else if (arr[0].equals("key"))
				category = Joiner.on('.').join(ArrayUtils.add(arr, 1, "categories"));
			else category = "key.categories.".concat(category);
		}
		return new KeyBinding(id, keyCode, category);
	}

	/**
	 * @param id       The id of the {@link KeyBinding}. Could be simple like "hit" or "toggle". Also key be default style,
	 *                 "key.xxx".
	 * @param keyCode  The Key code, see {@link org.lwjgl.input.Keyboard}.
	 * @param category The category of the {@link KeyBinding}.
	 * @param context  The conflict context of the {@link KeyBinding}.
	 * @return A new keyBinding
	 */
	public static KeyBinding create(String id, int keyCode, String category, IKeyConflictContext context)
	{
		KeyBinding binding = create(id, keyCode, category);
		binding.setKeyConflictContext(context);
		return binding;
	}
}
