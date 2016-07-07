package org.singularity.rune.test;

import api.simplelib.registry.components.ModComponent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author ci010
 */
@ModComponent
public class BlockTest extends Block
{
	public BlockTest()
	{
		super(Material.ROCK);
	}
}
