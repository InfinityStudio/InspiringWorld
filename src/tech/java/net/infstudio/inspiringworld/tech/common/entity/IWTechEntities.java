package net.infstudio.inspiringworld.tech.common.entity;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class IWTechEntities {

    public static void preInit() {
        IWTechEntities.registerEntity(EntitySourceBomb.class, "SourceBomb", Tracker.of(80, 4, true));
    }

    private static void registerEntity(Class<? extends Entity> entity, String name, Tracker tracker) {
        EntityRegistry.registerModEntity(entity, name, name.hashCode(), InspiringTech.instance, tracker.trackingRange,
            tracker.updateFrequency, tracker.sendVelocityUpdates);
    }

    private static void registerEntityEgg(Class<? extends Entity> entity, int primary, int secondary) {
        EntityRegistry.registerEgg(entity, primary, secondary);
    }

    private static class Tracker {
        public final int trackingRange;
        public final int updateFrequency;
        public final boolean sendVelocityUpdates;

        private Tracker(int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
            this.trackingRange = trackingRange;
            this.updateFrequency = updateFrequency;
            this.sendVelocityUpdates = sendsVelocityUpdates;
        }

        public static Tracker of(int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
            return new Tracker(trackingRange, updateFrequency, sendsVelocityUpdates);
        }
    }
}
