package net.infstudio.inspiringworld.tech.client.entity.render;

import net.infstudio.inspiringworld.tech.common.entity.EntityProducerBomb;
import net.infstudio.inspiringworld.tech.common.item.IWTechItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;

public class RenderProducerBomb extends RenderSnowball<EntityProducerBomb>
{
    public RenderProducerBomb(RenderManager renderManagerIn)
    {
        super(renderManagerIn, IWTechItems.producerBomb, Minecraft.getMinecraft().getRenderItem());
    }
}
