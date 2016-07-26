package net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading;

import net.minecraft.util.ResourceLocation;

import java.io.InputStream;
import java.util.Set;

/**
 * @author ci010
 */
public interface Pack
{
	InputStream getInputStream(ResourceLocation location);

	boolean resourceExists(ResourceLocation location);

	Set<String> domain();
}
