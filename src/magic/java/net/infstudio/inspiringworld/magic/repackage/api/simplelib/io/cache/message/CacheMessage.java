package net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache.message;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.io.cache.CacheInfo;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.network.simple.SimpleClientMessage;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.seril.ITagSerializer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

/**
 * @author ci010
 */
public class CacheMessage extends SimpleClientMessage<CacheInfo>
{
	public CacheMessage()
	{
		super();
	}

	public CacheMessage(CacheInfo info)
	{
		super(info);
	}

	@Override
	public ITagSerializer<CacheInfo> getSerializer()
	{
		return CacheInfo.nbtSeril;
	}

	@Override
	public IMessage handleClientMessage(EntityPlayer player, CacheInfo data)
	{
		CacheTypeChannel.instance().pipe(data);
		return null;
	}
}
