package net.infstudio.inspiringworld.magic.test;

import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ModComponent;
import net.infstudio.inspiringworld.magic.repackage.api.simplelib.registry.components.ModCreativeTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

/**
 * @author ci010
 */
@ModComponent
@ModCreativeTab(ModCreativeTab.BUILDING_BLOCKS)
public class BlockTest extends Block
{
	public BlockTest()
	{
		super(Material.ROCK);
	}
}
