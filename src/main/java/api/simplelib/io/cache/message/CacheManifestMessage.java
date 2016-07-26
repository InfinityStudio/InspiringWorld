package api.simplelib.io.cache.message;

import api.simplelib.io.cache.CacheManifest;
import api.simplelib.network.simple.SimpleClientMessage;
import api.simplelib.seril.ITagSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ci010
 */
public class CacheManifestMessage extends SimpleClientMessage<CacheManifest>
{
	public CacheManifestMessage()
	{}

	public CacheManifestMessage(CacheManifest manifest)
	{
		super(manifest);
	}

	@Override
	public ITagSerializer<CacheManifest> getSerializer()
	{
		return CacheManifest.nbtSerial;
	}

	@Override
	public IMessage handleClientMessage(EntityPlayer player, CacheManifest data)
	{
		CacheTypeChannel.instance().pipe(data);
		return null;
	}
}
