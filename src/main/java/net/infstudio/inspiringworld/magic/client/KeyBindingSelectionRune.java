package net.infstudio.inspiringworld.magic.client;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.client.KeyBindings;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.ModHandler;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

/**
 * @author ci010
 */
@ModHandler
public class KeyBindingSelectionRune
{
	public static final KeyBinding SHOW_RUNE_SELECT =
			KeyBindings.create("show_rune", Keyboard.KEY_R, KeyBindings.GAMEPLAY, KeyConflictContext.IN_GAME);


	@SubscribeEvent

	public void keyBindingEvent(InputEvent.KeyInputEvent event)
	{
		Overlay.INSTANCE.setEnable(SHOW_RUNE_SELECT.isPressed());
	}
}
