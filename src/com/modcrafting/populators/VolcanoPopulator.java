package com.modcrafting.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

public class VolcanoPopulator extends BlockPopulator{

	@Override
	public void populate(World world, Random random, Chunk chunk){
		int cx = chunk.getX() * 16;
		int cz = chunk.getZ() * 16;
		
		for (int x = 0; x < 16; ++x){
			for (int z = 0; z < 16; ++z){
				int y = world.getHighestBlockYAt(cx + x, cz + z);
				//this is not complete to say the least.
			}
		}
	}

}
