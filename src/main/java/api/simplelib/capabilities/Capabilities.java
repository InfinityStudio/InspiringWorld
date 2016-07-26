package api.simplelib.capabilities;

import api.simplelib.utils.ArrayUtils;
import api.simplelib.utils.TypeUtils;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import java.util.EnumMap;
import java.util.Map;

/**
 * @author ci010
 */
public class Capabilities
{
	public static class Builder<T>
	{
		public final Capability<T> capability;
		private EnumMap<EnumFacing, T> instances = new EnumMap<EnumFacing, T>(EnumFacing.class);
		private T single;

		public Builder(Capability<T> capability) {this.capability = capability;}

		public Builder<T> append(T instance, EnumFacing... facings)
		{
			Preconditions.checkNotNull(instance);
			if (facings == null)
				if (instances.isEmpty())
					if (single == null)
						single = instance;
					else throw new IllegalArgumentException("Cannot add two free instance to all facing for one " +
							"Capability!");
				else throw new IllegalArgumentException("All faces are occupied! cannot add new instance.");
			else
			{
				for (EnumFacing facing : facings)
					if (!instances.containsKey(facing))
						instances.put(facing, instance);
					else throw new IllegalArgumentException("The face " + facing + " was already occupied!");
			}
			return this;
		}

		public Builder<T> appendDefaultInstance(EnumFacing... facings)
		{
			return append(capability.getDefaultInstance(), facings);
		}

		public ICapabilityProvider buildUnsaved()
		{
			if (instances.isEmpty() && single != null)
				return new Common<T>(single, capability);
			return new Sided<T>(capability, this.instances);
		}

		public ICapabilitySerializable<NBTBase> build()
		{
			if (instances.isEmpty() && single != null)
				return new CommonSerial<T>(single, capability);
			return new SidedSerial<T>(capability, this.instances);
		}
	}

	public static class BuilderInContext<T extends Predicate<C>, C>
	{
		private C context;
		public final Capability<T> capability;
		private EnumMap<EnumFacing, T> instances = new EnumMap<EnumFacing, T>(EnumFacing.class);
		private T single;

		private BuilderInContext(Capability<T> capability, C context)
		{
			this.context = context;
			this.capability = capability;
		}

		public boolean append(T instance, EnumFacing... facings)
		{
			Preconditions.checkNotNull(instance);
			boolean pass = instance.apply(context);
			if (!pass) return false;
			if (facings == null)
				if (instances.isEmpty())
					if (single == null)
						single = instance;
					else throw new IllegalArgumentException("Cannot add two free instance to all facing for one " +
							"Capability!");
				else throw new IllegalArgumentException("All faces are occupied! cannot add new instance.");
			else
				for (EnumFacing facing : facings)
					if (!instances.containsKey(facing))
						instances.put(facing, instance);
					else throw new IllegalArgumentException("The face " + facing + " was already occupied!");
			return true;
		}


		public boolean appendDefaultInstance(EnumFacing... facings)
		{
			return append(capability.getDefaultInstance(), facings);
		}

		public ICapabilityProvider build()
		{
			if (instances.isEmpty() && single != null)
				return new Common<T>(single, capability);
			return new Sided<T>(capability, this.instances);
		}

		public ICapabilitySerializable<NBTBase> buildSerializable()
		{
			if (instances.isEmpty() && single != null)
				return new CommonSerial<T>(single, capability);
			return new SidedSerial<T>(capability, this.instances);
		}
	}

	public static CapabilityDispatcher gatherCapabilities(AttachCapabilitiesEvent event)
	{
		MinecraftForge.EVENT_BUS.post(event);
		return event.getCapabilities().size() > 0 ? new CapabilityDispatcher(event.getCapabilities()) : null;
	}

	public static <T> Builder<T> newBuilder(Capability<T> capability)
	{
		return new Builder<T>(capability);
	}

	public static <T extends Predicate<ItemStack>> BuilderInContext<T, ItemStack> newBuilder(Capability<T> capability, ItemStack stack)
	{
		return new BuilderInContext<T, ItemStack>(capability, stack);
	}

	public static <T extends Predicate<Entity>> BuilderInContext<T, Entity> newBuilder(Capability<T> capability, Entity entity)
	{
		return new BuilderInContext<T, Entity>(capability, entity);
	}

	public static <T extends Predicate<TileEntity>> BuilderInContext<T, TileEntity> newBuilder(Capability<T> capability, TileEntity entity)
	{
		return new BuilderInContext<T, TileEntity>(capability, entity);
	}

	/**
	 * Get the capability instance ignoring side.
	 *
	 * @param capability The capability.
	 * @param provider   The capability source.
	 * @param <T>        The type of the capability.
	 * @return The capability instance.
	 */
	public static <T> T get(Capability<T> capability, ICapabilityProvider provider)
	{
		return provider.getCapability(capability, null);
	}

	public static Map<ResourceLocation, ICapabilityProvider> revolve(Entity entity, Object target)
	{
		return CapabilityInterfaceInject.instance().handle(entity, target);
	}

	public static Map<ResourceLocation, ICapabilityProvider> revolve(ItemStack stack, Object target)
	{
		return CapabilityInterfaceInject.instance().handle(stack, target);
	}

	public static Map<ResourceLocation, ICapabilityProvider> revolve(TileEntity tileEntity, Object target)
	{
		return CapabilityInterfaceInject.instance().handle(tileEntity, target);
	}

	public static class Common<T> implements ICapabilityProvider
	{
		protected T real;
		protected Capability<T> capability;

		public Common(T real, Capability<T> capability)
		{
			this.real = real;
			this.capability = capability;
		}

		public Common(Capability<T> capability)
		{
			this.capability = capability;
			this.real = capability.getDefaultInstance();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return capability == this.capability;
		}

		@Override
		public <C> C getCapability(Capability<C> capability, EnumFacing facing)
		{
			if (hasCapability(capability, facing))
				return TypeUtils.cast(real);
			return null;
		}
	}

	public static class CommonSerial<T> extends Common<T> implements ICapabilitySerializable<NBTBase>
	{
		public CommonSerial(T real, Capability<T> capability)
		{
			super(real, capability);
		}

		public CommonSerial(Capability<T> capability)
		{
			super(capability);
		}

		@Override
		public NBTBase serializeNBT()
		{
			return capability.getStorage().writeNBT(capability, this.real, null);
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			this.capability.getStorage().readNBT(capability, real, null, nbt);
		}
	}

	public static class Sided<T> implements ICapabilityProvider
	{
		protected Capability<T> capability;
		protected T instances[];

		public Sided(Capability<T> capability, EnumMap<EnumFacing, T> sideMap)
		{
			this.capability = capability;
			instances = ArrayUtils.newArrayWithCapacity(EnumFacing.values().length);
			for (Map.Entry<EnumFacing, T> entry : sideMap.entrySet())
				instances[entry.getKey().ordinal()] = entry.getValue();
		}

		public Sided(Capability<T> capability, EnumFacing... side)
		{
			EnumMap<EnumFacing, T> sideMap = new EnumMap<EnumFacing, T>(EnumFacing.class);
			if (side == null || side.length == 0)
				for (EnumFacing value : EnumFacing.VALUES)
					sideMap.put(value, capability.getDefaultInstance());
			else
				for (EnumFacing facing : side)
					sideMap.put(facing, capability.getDefaultInstance());
			this.capability = capability;
			instances = ArrayUtils.newArrayWithCapacity(EnumFacing.values().length);
			for (Map.Entry<EnumFacing, T> entry : sideMap.entrySet())
				instances[entry.getKey().ordinal()] = entry.getValue();
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return this.capability == capability && instances[facing.ordinal()] != null;
		}

		@Override
		public <C> C getCapability(Capability<C> capability, EnumFacing facing)
		{
			return TypeUtils.cast(instances[facing.ordinal()]);
		}
	}

	public static class SidedSerial<T> extends Sided<T> implements ICapabilitySerializable<NBTBase>
	{
		public SidedSerial(Capability<T> capability, EnumMap<EnumFacing, T> sideMap)
		{
			super(capability, sideMap);
		}

		public SidedSerial(Capability<T> capability, EnumFacing... side)
		{
			super(capability, side);
		}

		@Override
		public NBTBase serializeNBT()
		{
			Capability.IStorage<T> storage = capability.getStorage();
			NBTTagCompound tag = new NBTTagCompound();
			for (int i = 0; i < this.instances.length; i++)
				tag.setTag(EnumFacing.VALUES[i].getName(), storage.writeNBT(capability, instances[i], EnumFacing.VALUES[i]));
			return tag;
		}

		@Override
		public void deserializeNBT(NBTBase nbt)
		{
			if (nbt instanceof NBTTagCompound)
			{
				Capability.IStorage<T> storage = capability.getStorage();
				for (int i = 0; i < this.instances.length; i++)
					storage.readNBT(capability, instances[i], EnumFacing.VALUES[i], ((NBTTagCompound) nbt).getTag
							(EnumFacing.VALUES[i].getName()));
			}
		}
	}

	private Capabilities() {}
}
