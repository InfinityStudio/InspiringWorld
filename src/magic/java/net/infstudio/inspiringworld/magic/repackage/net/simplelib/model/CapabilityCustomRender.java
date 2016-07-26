package net.infstudio.inspiringworld.magic.repackage.net.simplelib.model;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.ICapability;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.capabilities.ModCapability;
import net.infstudio.inspiringworld.magic.repackage.net.simplelib.model.test.RenderPlayerCustom;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.concurrent.Callable;

/**
 * @author ci010
 */
@ModCapability
@SideOnly(Side.CLIENT)
public class CapabilityCustomRender implements ICapability<RenderPlayerCustom>
{
	@CapabilityInject(RenderPlayerCustom.class)
	public static Capability<RenderPlayerCustom> RENDER_CAP = null;

	@Override
	public Capability.IStorage<RenderPlayerCustom> storage()
	{
		return new Capability.IStorage<RenderPlayerCustom>() {
			@Override
			public NBTBase writeNBT(Capability<RenderPlayerCustom> capability, RenderPlayerCustom instance, EnumFacing side)
			{
				return null;
			}

			@Override
			public void readNBT(Capability<RenderPlayerCustom> capability, RenderPlayerCustom instance, EnumFacing side, NBTBase nbt)
			{

			}
		};
	}

	@Override
	public Callable<RenderPlayerCustom> factory()
	{
		return null;
	}

	@Override
	public Class<RenderPlayerCustom> type()
	{
		return null;
	}
}
