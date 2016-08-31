package net.infstudio.inspiringworld.tech.common.tileentity;

import net.infstudio.inspiringworld.tech.InspiringTech;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Blealtan
 */
public class IWTechTileEntities {

    public static void preInit() {
        registerTileEntity(TileEntitySourceLight.class, "SourceLight");
        registerTileEntity(TileEntityAntenna.class, "Antenna");
        registerTileEntity(TileEntitySourceAntenna.class, "SourceAntenna");
        registerTileEntity(TileEntityAbyssAntenna.class, "AbyssAntenna");
        registerTileEntity(TileEntityInspiringFurnace.class, "InspiringFurnace");
    }

    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        GameRegistry.registerTileEntity(tileEntityClass, InspiringTech.MODID + ":" + id);
    }
}
