package com.modcrafting.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class FlowerPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int cx = (chunk.getX() << 4) + x;
                int cz = (chunk.getZ() << 4) + z;
                int y = world.getHighestBlockYAt(cx, cz);

                Block block = chunk.getBlock(x, y, z);
                if (block.getType() == Material.AIR && block.getRelative(BlockFace.DOWN).getType() == Material.GRASS) {
                    if (block.getBiome() == Biome.PLAINS) {
                        int n = random.nextInt(128);
                        if (n < 1) {
                            block.setType(Material.RED_ROSE);
                        } else if (n < 4) {
                            block.setType(Material.YELLOW_FLOWER);
                        }
                    } else if (block.getBiome() == Biome.JUNGLE) {
                        int n = random.nextInt(512);
                        if (n < 2) {
                            block.setType(Material.RED_ROSE);
                        } else if (n < 3) {
                            block.setType(Material.YELLOW_FLOWER);
                        }
                    }
                }
            }
        }
    }
    
}
