package com.modcrafting.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

public class GrassPopulator extends BlockPopulator {
	@Override
	public void populate(World world, Random random, Chunk chunk){
		int x, y, z;
		Block block, ground;
		Biome biome;
		
		for (x = 0; x < 16; ++x){
			for (z = 0; z < 16; ++z){
				int rx = x + chunk.getX() * 16;
				int rz = z + chunk.getZ() * 16;
				y = world.getHighestBlockYAt(rx,rz);
				
				if (y > 75){
					block = chunk.getBlock(x, y, z);
					ground = block.getRelative(BlockFace.DOWN);
					biome = world.getBiome(rx,rz);
					
					if (ground.getType() == Material.GRASS){
						if (biome == Biome.PLAINS){
							if (random.nextInt(100) < 35){
								block.setType(Material.LONG_GRASS);
								block.setData((byte) 0x1);
							}
						
						}else if (biome == Biome.TAIGA || biome == Biome.TAIGA_HILLS){
							if (random.nextInt(100) < 1){
								block.setType(Material.LONG_GRASS);
								block.setData((byte) 0x2);
							}
						}else if (biome == Biome.ICE_PLAINS || biome == Biome.ICE_MOUNTAINS){
							if (random.nextInt(100) < 1){
								block.setType(Material.SNOW);
								block.setData((byte) 0x0);
							}
						}else{
							if (random.nextInt(100) < 14){
								block.setType(Material.LONG_GRASS);
							block.setData((byte) 0x1);
								block.setData((byte) ((random.nextInt(100) < 85) ? 0x1 : 0x2));
							}
						}
					}
					if (ground.getType() == Material.SAND){
						if (biome == Biome.DESERT){
							if (random.nextInt(100) < 3) block.setType(Material.DEAD_BUSH);
						}else{
							if (random.nextInt(100) < 14) block.setType(Material.DEAD_BUSH);						
						}
					}
				}
			}
		}
	}

}
