package net.infstudio.inspiringworld.tech.client.entity;

import com.google.common.base.Throwables;

import net.infstudio.inspiringworld.tech.client.entity.render.RenderProducerBomb;
import net.infstudio.inspiringworld.tech.common.entity.EntityProducerBomb;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class IWTechEntityRender {
    public static void preInit() {
        IWTechEntityRender.registerEntityRender(EntityProducerBomb.class, RenderProducerBomb.class);
    }

    @SideOnly(Side.CLIENT)
    private static <T extends Entity> void registerEntityRender(Class<T> entity, Class<? extends Render<T>> render) {
        RenderingRegistry.registerEntityRenderingHandler(entity, new EntityRenderFactory<T>(render));
    }

    private static class EntityRenderFactory<T extends Entity> implements IRenderFactory<T> {
        private final Class<? extends Render<T>> renderClass;

        public EntityRenderFactory(Class<? extends Render<T>> renderClass) {
            this.renderClass = renderClass;
        }

        @Override
        public Render<T> createRenderFor(RenderManager manager) {
            try {
                return this.renderClass.getConstructor(RenderManager.class).newInstance(manager);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }
        }
    }
}
