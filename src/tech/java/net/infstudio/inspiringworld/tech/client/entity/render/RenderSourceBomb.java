package net.infstudio.inspiringworld.tech.client.entity.render;

import net.infstudio.inspiringworld.tech.common.entity.EntitySourceBomb;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;

public class RenderSourceBomb extends RenderSnowball<EntitySourceBomb>
{
    public RenderSourceBomb(RenderManager renderManagerIn)
    {
        super(renderManagerIn, IWTechItems.sourceBomb, Minecraft.getMinecraft().getRenderItem());
    }
}
