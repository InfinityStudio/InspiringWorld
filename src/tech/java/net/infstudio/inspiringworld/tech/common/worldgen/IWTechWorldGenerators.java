package net.infstudio.inspiringworld.tech.common.worldgen;

import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Blealtan
 */
public class IWTechWorldGenerators {

    public static void preInit() {
        new EnderTreeGen(false).applyToVanilla();

        GameRegistry.registerWorldGenerator(new EnderSpiderSpawnerGen(), 1);
    }
}
