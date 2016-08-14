package net.infstudio.inspiringworld.tech.common.worldgen;

/**
 * @author Blealtan
 */
public class IWTechWorldGenerators {

    public static final EnderTreeGen enderTreeGen = new EnderTreeGen(false);

    public static void preInit() {
        enderTreeGen.applyToVanilla();
    }
}
