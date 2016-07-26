package api.simplelib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;

/**
 * @author ci010
 */

public class ContainerLegacy extends Container
{
	private Field[] watching;
	private int[] memory;

	protected TileEntity tile;

	public ContainerLegacy(TileEntity tile, InventoryPlayer inv)
	{
		this.tile = tile;
		this.watching = Watching.Utils.scan(tile);
		this.memory = new int[watching.length];
		if (inv != null)
		{
			int index;
			for (index = 0; index < 3; ++index)
				for (int offset = 0; offset < 9; ++offset)
					this.addSlotToContainer(new Slot(inv, offset + index * 9 + 9, 8 + offset * 18, 84 + index * 18));
			for (index = 0; index < 9; ++index)
				this.addSlotToContainer(new Slot(inv, index, 8 + index * 18, 142));
		}
	}


	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return true;
	}

	private int get(int i)
	{
		try
		{
			return (Integer) watching[i].get(tile);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		return memory[i];
	}

	private void set(int i, int v)
	{
		try
		{
			watching[i].set(tile, v);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		memory[i] = v;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int value)
	{
		if (id < watching.length)
			set(id, value);
	}

	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); ++i)
		{
			IContainerListener icrafting = this.listeners.get(i);
			int count = 0;
			for (int j = 0; j < watching.length; j++)
			{
				int currentValue = this.get(j);
				if (memory[count] != currentValue)
					icrafting.sendProgressBarUpdate(this, count, currentValue);
				memory[j] = currentValue;
			}
		}
	}
}
