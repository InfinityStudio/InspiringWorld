package net.infstudio.inspiringworld.tech.common.worldgen;

import java.util.Random;

import net.infstudio.inspiringworld.tech.common.config.IWTechConfig;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * @author Blealtan
 */
public class EnderSpiderSpawnerGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
        IChunkProvider chunkProvider) {
        for (TileEntity tileEntity : world.getChunkFromChunkCoords(chunkX, chunkZ).getTileEntityMap().values()) {
            if (tileEntity instanceof TileEntityMobSpawner) {
                MobSpawnerBaseLogic spawner = ((TileEntityMobSpawner) tileEntity).getSpawnerBaseLogic();
                // Reflect to get the name
                try {
                    String name = (String) ReflectionHelper.findMethod(MobSpawnerBaseLogic.class, spawner,
                        new String[] { "getEntityNameToSpawn", "func_98276_e" }).invoke(spawner);
                    if (name.equals("CaveSpider") && random.nextInt(4) == 0) {
                        spawner.setEntityName("EnderSpider");
                    }
                } catch (Exception e) {
                    IWTechConfig.logger().warn("InspiringWorld EnderSpider spawner generator's reflection failed:", e);
                }
            }
        }
    }
}
