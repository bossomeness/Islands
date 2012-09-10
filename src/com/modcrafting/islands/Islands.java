package com.modcrafting.islands;

import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;

public class Islands extends JavaPlugin{
	public ChunkGenerator getDefaultWorldGenerator(String worldName, String GenId) {
		return new com.modcrafting.islands.Chunker();
	}
	
}
