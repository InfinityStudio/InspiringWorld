package net.infstudio.inspiringworld.magic.client;

import net.infstudio.inspiringworld.magic.Rune;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.*;

/**
 * @author ci010
 */
@SideOnly(Side.CLIENT)
public class RuneGuiComp
{
	private int weight;
	private Rune rune;
	private int toggleRange;
	private Polygon polygon;

	public RuneGuiComp(Rune rune, int weight, int toggleRange)
	{
		this.weight = weight;
		this.rune = rune;
		this.toggleRange = toggleRange;
	}

	public Polygon getRange()
	{
		return polygon;
	}

	public int getWeight()
	{
		return weight;
	}

	public Rune getRune()
	{
		return rune;
	}

	public int getToggleRange()
	{
		return toggleRange;
	}
}
