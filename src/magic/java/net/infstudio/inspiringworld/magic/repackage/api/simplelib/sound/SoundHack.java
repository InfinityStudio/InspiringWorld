package net.infstudio.inspiringworld.magic.repackage.api.simplelib.sound;

import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.midi.Sequencer;
import java.util.Map;

/**
 * @author ci010
 */
@SideOnly(Side.CLIENT)
public class SoundHack
{
	private static SoundHack INSTANCE;

	private SoundHack()
	{
		hack();
	}

	private void hack()
	{
		SoundCategory bgm1 = EnumHelper.addEnum(SoundCategory.class, "BGM1", new Class[]{String.class}, "bgm1");
		SoundCategory bgm2 = EnumHelper.addEnum(SoundCategory.class, "BGM2", new Class[]{String.class}, "bgm2");
		Map<String, SoundCategory> NAME_CATEGORY_MAP = ReflectionHelper.getPrivateValue(SoundCategory.class, null,
				"SOUND_CATEGORIES");
		NAME_CATEGORY_MAP.put(bgm1.getName(), bgm1);
		NAME_CATEGORY_MAP.put(bgm2.getName(), bgm2);
	}

	public static SoundHack instance()
	{
		if (INSTANCE == null)
			INSTANCE = new SoundHack();
		return INSTANCE;
	}
}
