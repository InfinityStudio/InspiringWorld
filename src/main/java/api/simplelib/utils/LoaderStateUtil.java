package api.simplelib.utils;

import com.google.common.collect.EnumHashBiMap;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.event.*;

/**
 * @author ci010
 */
public class LoaderStateUtil
{
	private static EnumHashBiMap<LoaderState, Class<? extends FMLStateEvent>> states = EnumHashBiMap.create
			(LoaderState
					.class);

	static
	{
		states.put(LoaderState.CONSTRUCTING, FMLConstructionEvent.class);
		states.put(LoaderState.PREINITIALIZATION, FMLPreInitializationEvent.class);
		states.put(LoaderState.INITIALIZATION, FMLInitializationEvent.class);
		states.put(LoaderState.POSTINITIALIZATION, FMLPostInitializationEvent.class);
		states.put(LoaderState.AVAILABLE, FMLLoadCompleteEvent.class);
		states.put(LoaderState.SERVER_ABOUT_TO_START, FMLServerAboutToStartEvent.class);
		states.put(LoaderState.SERVER_STARTING, FMLServerStartingEvent.class);
		states.put(LoaderState.SERVER_STARTED, FMLServerStartedEvent.class);
		states.put(LoaderState.SERVER_STOPPING, FMLServerStoppingEvent.class);
		states.put(LoaderState.SERVER_STOPPED, FMLServerStoppedEvent.class);
	}

	/**
	 * @param state A {@link FMLStateEvent}
	 * @return The {@link LoaderState} referred by that {@link FMLStateEvent}.
	 */
	public static LoaderState getState(Class<? extends FMLStateEvent> state)
	{
		return states.inverse().get(state);
	}

	/**
	 * @param state A {@link LoaderState}
	 * @return The {@link FMLStateEvent} referred by that {@link LoaderState}.
	 */
	public static Class<? extends FMLStateEvent> getState(LoaderState state)
	{
		return states.get(state);
	}
}
