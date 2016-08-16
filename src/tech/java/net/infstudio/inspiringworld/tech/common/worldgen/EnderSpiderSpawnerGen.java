package net.infstudio.inspiringworld.tech.common.worldgen;

import java.lang.reflect.Method;
import java.util.Random;

import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.IWorldGenerator;

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
                String name = "";
                // Reflect to get the name
                try {
                    Method method = MobSpawnerBaseLogic.class.getMethod("getEntityNameToSpawn");
                    method.setAccessible(true);
                    name = (String) method.invoke(spawner);
                } catch (Throwable e) {
                    FMLLog.getLogger().warn("InspiringWorld EnderSpider spawner generator's reflection failed:", e);
                }
                if (name.equals("CaveSpider") && random.nextInt(4) == 0) {
                    spawner.setEntityName("EnderSpider");
                }
            }
        }
    }
}
