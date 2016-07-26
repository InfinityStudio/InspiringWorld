package net.infstudio.inspiringworld.magic.repackage.api.simplelib.network;

public abstract class AbstractBiMessage<T> extends AbstractMessage<T>
{
	public AbstractBiMessage(MessageCoder<T> coder)
	{
		super(coder);
	}
}
