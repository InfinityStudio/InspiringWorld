package api.simplelib.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * For key code, see {@link org.lwjgl.input.Keyboard}.
 *
 * @author ci010
 * @see KeyBinding
 * @see org.lwjgl.input.Keyboard
 */
public class KeyBindingEvent extends Event
{
	private KeyBinding keyBinding;

	public KeyBindingEvent(KeyBinding keyBinding)
	{
		this.keyBinding = keyBinding;
	}

	public KeyBinding getKeyBinding()
	{
		return keyBinding;
	}
}
