package net.simplelib.common.registry;

import api.simplelib.KeyBindingEvent;
import api.simplelib.registry.ModHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;


/**
 * @author ci010
 */
@ModHandler
@SideOnly(Side.CLIENT)
public class KeyBindingPoster
{
	private final List<KeyBinding> keybindArray = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "KEYBIND_ARRAY");

	@SubscribeEvent
	public void onKeyDown(InputEvent.KeyInputEvent event)
	{
		KeyBinding binding;
		for (int i = 0; i < keybindArray.size(); i++)
			if ((binding = keybindArray.get(i)).isKeyDown())
				MinecraftForge.EVENT_BUS.post(new KeyBindingEvent(binding));
	}
}
