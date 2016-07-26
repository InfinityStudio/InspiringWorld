package net.infstudio.inspiringworld.magic.repackage.net.simplelib.model.test;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

/**
 * @author ci010
 */
public class RenderPlayerCustom extends RenderPlayer
{
	public RenderPlayerCustom(RenderManager renderManager)
	{
		super(renderManager, false);
		this.mainModel = new ModelPlayerCustom(1, false);
	}
}
