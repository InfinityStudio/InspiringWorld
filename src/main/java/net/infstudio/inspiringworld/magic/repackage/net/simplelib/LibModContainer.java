package net.infstudio.inspiringworld.magic.repackage.net.simplelib;

import com.google.common.eventbus.EventBus;

import net.infstudio.inspiringworld.magic.repackage.net.simplelib.client.loading.ExternalResource;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.common.DebugLogger;
import net.minecraftforge.fml.client.SplashProgress;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

/**
 * @author ci010
 */
public class LibModContainer extends DummyModContainer
{
	public LibModContainer()
	{
		super(new ModMetadata());
		ModMetadata meta = this.getMetadata();
		meta.modId = "lib-helper";
		meta.authorList.add("ci010");
		meta.name = "Lib-helper";
		this.startToHackScreen();
	}

	void startToHackScreen()
	{
		SplashProgress.pause();
		DebugLogger.info("Start to check mods updatable.");
//		new Checker().run();
		SplashProgress.resume();
	}

	@Override
	public Object getMod()
	{
		return this;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller)
	{
		return true;
	}

	@Override
	public Class<?> getCustomResourcePackClass()
	{
		return ExternalResource.class;
	}
}
