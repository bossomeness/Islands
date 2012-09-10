package com.modcrafting.islands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.PerlinOctaveGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import com.modcrafting.populators.CavePopulator;
import com.modcrafting.populators.DungeonPopulator;
import com.modcrafting.populators.GrassPopulator;
import com.modcrafting.populators.OrePopulator;
import com.modcrafting.populators.TreePopulator;

public class Chunker extends ChunkGenerator {
	void setBlock(int x, int y, int z, byte[][] chunk, Material material) {
		if (!(x < 16 && x >= 0 && z < 16 && z >= 0 && y < 256 && y >=0 )) return;
		if (chunk[y >> 4] == null)
			chunk[y >> 4] = new byte[16 * 16 * 16];
		if (!(y <= 256 && y >= 0 && x <= 16 && x >= 0 && z <= 16 && z >= 0))
			return;
		try {
			chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x] = (byte) material
					.getId();
		} catch (Exception e) {
		}
	}
	byte getBlock(int x, int y, int z, byte[][] chunk) {
		if (!(y < 256 && y >= 0 && x < 16 && x >= 0 && z < 16 && z >= 0))
			return 0;
		if (chunk[y >> 4] == null)
			return 0;
		try {
			return chunk[y >> 4][((y & 0xF) << 8) | (z << 4) | x];
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	@Override
	public byte[][] generateBlockSections(World world, Random rand, int ChunkX, int ChunkZ, BiomeGrid biome) {
		byte[][] chunk = new byte[world.getMaxHeight() / 16][];
		//Base
		SimplexOctaveGenerator bg = new SimplexOctaveGenerator(world,18);
		bg.setScale(1/128.0);
		//Alt
		SimplexOctaveGenerator g = new SimplexOctaveGenerator(world,36);
		g.setScale(1/64.0);
		//Peak
		PerlinOctaveGenerator e = new PerlinOctaveGenerator(world,32);
		e.setScale(1/32.0);
		//Razer Basin
		PerlinOctaveGenerator c = new PerlinOctaveGenerator(world,38);
		c.setScale(1/64.0);
		//Gun
		SimplexOctaveGenerator d = new SimplexOctaveGenerator(world,18);
		d.setScale(1/128.0);
		
		int max = world.getMaxHeight();
		for (int x=0; x<16; x++) {
			for (int z=0; z<16; z++) {
				Biome b = biome.getBiome(x,z);
				int rx = x + ChunkX * 16;
				int rz = z + ChunkZ * 16;
				int h = (int) ((bg.noise(rx, rz, 0.75, 0.75)*38+58));
				int bh = (int) ((g.noise(rx, rz, 0.5, 0.5)*24+56));
				int ch = (int) ((c.noise(rx, rz, 0.75, 0.25)+g.noise(rx, rz, 1.25, 0.75)+bg.noise(rx, rz, 0.25, 0.5)*18+20));
				int eg = (int) ((e.noise(rx, rz, 0.5, 0.5)+g.noise(rx, rz, 1.25, 0.75)*38+64));
				int ds = (int) ((d.noise(rx, rz, 0.25, 0.75)+c.noise(rx, rz, 0.75, 0.25)+g.noise(rx, rz, 1.25, 0.75)+bg.noise(rx, rz, 0.25, 0.5)*32+25));
				//Full Pass
				for (int y=2; y<max; y++) {
					if(y<h||y<bh||y<eg) setBlock(x,y,z,chunk,Material.STONE);
					//Shave
					if(y>(ch*-1)+95||y>(ds*-1)+101) setBlock(x,y,z,chunk,Material.AIR);
					//Cleaner	
					if(y>70){
						int it = 0;
						int maxX = Math.max(x+1, x-1);
						int maxY = Math.max(y+1, y-1);
						int maxZ = Math.max(z+1, z-1);
						int minX = Math.max(x+1, x-1);
						int minY = Math.max(y+1, y-1);
						int minZ = Math.max(z+1, z-1);
						for(int i=0;i<=Math.abs(maxX-minX);i++){
							for(int ii=0;ii<=Math.abs(maxZ-minZ);ii++){
								for(int iii=0;iii<=Math.abs(maxY-minY);iii++){
									int bt = getBlock(minX+i, minY+iii, minZ+ii, chunk);
									if(bt==Material.AIR.getId()||bt==Material.STATIONARY_WATER.getId())it++;
								}
							}
						}
						if(it>5) setBlock(x,y,z,chunk,Material.AIR);
					}
				}
				//Overlay Land Pass
				//TODO Remind myself to remove these for loops and change to if statments from the intial pass.
				Material b1, b2;
				if (b.equals(Biome.DESERT)||b.equals(Biome.DESERT_HILLS)||b.equals(Biome.BEACH)){
					b1 = Material.SAND;
					b2 = Material.SANDSTONE;
				}else{
					b1 = Material.GRASS;
					b2 = Material.DIRT;
				}
				for (int y=74; y<max; y++ ) {
					int thisblock = getBlock(x, y, z, chunk);
                    int blockabove = getBlock(x, y+1, z, chunk);
                    if(thisblock != Material.AIR.getId() && blockabove == Material.AIR.getId()){
                        setBlock(x, y, z, chunk, b1);
                        int y1 = (rand.nextInt(5)+1);
                        int y2 = (rand.nextInt(5)+1);
                        if(y<128){
                            if(getBlock(x, y-y1, z, chunk) != Material.AIR.getId()) setBlock(x, y-y1, z, chunk, b2);
                            if(getBlock(x, y-y2, z, chunk) != Material.AIR.getId()) setBlock(x, y-y2, z, chunk, b2);
                        }
                    }
				}
				//Ocean Pass
				for(int y=1;y<75;y++){
					int thisblock = getBlock(x, y, z, chunk);
					if(thisblock==Material.AIR.getId()){
                        setBlock(x, y, z, chunk, Material.STATIONARY_WATER);
                        int blockbelow = getBlock(x, y-1, z, chunk);
                        if(blockbelow == Material.GRASS.getId()) setBlock(x,y-1,z,chunk,Material.SAND);
					}
				}
				//Voidtector
				setBlock(x,0,z,chunk,Material.BEDROCK);
			}
		}
		return chunk;
	}
	
    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        ArrayList<BlockPopulator> pops = new ArrayList<BlockPopulator>();
        pops.add(new TreePopulator());
        pops.add(new DungeonPopulator());
        pops.add(new CavePopulator());
        pops.add(new OrePopulator());
        pops.add(new GrassPopulator());
        //Soon my pretties.
        //pops.add(new VolcanoPopulator());
        return pops;
    }
}
