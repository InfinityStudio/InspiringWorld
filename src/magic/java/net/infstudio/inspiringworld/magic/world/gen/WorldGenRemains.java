package net.infstudio.inspiringworld.magic.world.gen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenRemains implements IWorldGenerator{
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider) {
        this.generatePool(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
    }
    
    private void generatePool(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
            IChunkProvider chunkProvider) {
        //if(chunkX%(1000/16) != 0|| chunkZ%(1000/16) != 0) return;
        if(random.nextInt(1000)>100) return;
        int x = chunkX*16 + random.nextInt(16);
        int z = chunkZ*16 + random.nextInt(16);
        int y = world.getPrecipitationHeight(new BlockPos(x,0,z)).getY();
        for(int i=0; i<22; i++) {
            for(int j=0; j<22; j++) {
                world.setBlockState(new BlockPos(x+i,y,z+j), Blocks.CLAY.getDefaultState());
            }
        }
    }
}
