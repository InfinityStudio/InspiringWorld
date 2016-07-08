package net.simplelib.common.registry;

import api.simplelib.client.KeyBindings;
import api.simplelib.registry.ModHandler;
import com.google.common.collect.Sets;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Set;


/**
 * @author ci010
 */
@ModHandler
@SideOnly(Side.CLIENT)
public class KeyBindingPoster
{
	private final List<KeyBinding> keybindArray = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "KEYBIND_ARRAY");

	private Set<KeyBinding> pressing = Sets.newHashSet();
	private Set<KeyBinding> removed = Sets.newHashSet();

	@SubscribeEvent
	public void onKeyDown(InputEvent.KeyInputEvent event)
	{
		KeyBinding binding;
		if (!pressing.isEmpty())
			for (KeyBinding keyBinding : pressing)
				if (!keyBinding.isKeyDown())
					removed.add(keyBinding);
		for (int i = 0; i < keybindArray.size(); i++)
			if ((binding = keybindArray.get(i)).isKeyDown())
				if (!pressing.contains(binding))
				{
					MinecraftForge.EVENT_BUS.post(new KeyBindings.KeyBindingEvent(binding, KeyBindings.KeyEventType.PRESS));
					pressing.add(binding);
				}
		for (KeyBinding keyBinding : removed)
		{
			pressing.remove(keyBinding);
			MinecraftForge.EVENT_BUS.post(new KeyBindings.KeyBindingEvent(keyBinding, KeyBindings.KeyEventType.RELEASE));
		}
		removed.clear();
	}
}
