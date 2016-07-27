package net.infstudio.inspiringworld.magic.repackage.api.simplelib.lang;

import com.google.common.collect.Maps;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.registry.LanguageReporter;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;
import java.util.UUID;

/**
 * @author ci010
 */
public class Local
{
	private static Map<UUID, String> langMap = Maps.newHashMap();

	public static String getPlayerLang(EntityPlayerMP mp)
	{
		if (langMap.containsKey(mp.getGameProfile().getId()))
			return langMap.get(mp.getGameProfile().getId());
		String lang = ReflectionHelper.getPrivateValue(EntityPlayerMP.class, mp, "translator");
		langMap.put(mp.getGameProfile().getId(), lang);
		return lang;
	}

	public static String trans(String id)
	{
		return trans(id, id);
	}

	public static String trans(String id, String fallback)
	{
		if (I18n.hasKey(id))
			return I18n.format(id);
		LanguageReporter.instance().report(id);
		return fallback;
	}

	public static ITextComponent newChat(String id)
	{
		return new TextComponentString(trans(id));
	}
}
