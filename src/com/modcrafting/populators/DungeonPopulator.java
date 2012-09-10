package com.modcrafting.populators;

import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.util.noise.SimplexNoiseGenerator;
public class DungeonPopulator extends BlockPopulator {
    private SimplexNoiseGenerator simplex;
    private Random random;
    private World world;

    @Override
    public void populate(World w, Random rnd, Chunk chunk) {
        simplex = new SimplexNoiseGenerator(rnd);
        random = rnd;
        world = w;
        double density = simplex.noise(chunk.getX() * 16, chunk.getZ() * 16);
        if (density > 0.8) {
            int roomCount = (int) (density * 10) - 3;

            for (int i = 0; i < roomCount; i++) {
                if (random.nextBoolean()) {
                    int x = (chunk.getX() << 4) + random.nextInt(16);
                    int z = (chunk.getZ() << 4) + random.nextInt(16);
                    int y = 12 + random.nextInt(22);

                    int sizeX = random.nextInt(12) + 5;
                    int sizeY = random.nextInt(6) + 4;
                    int sizeZ = random.nextInt(12) + 5;
                    
                    generateRoom(x, y, z, sizeX, sizeY, sizeZ);
                }
            }
        }
    }

    private void generateRoom(int posX, int posY, int posZ, int sizeX, int sizeY, int sizeZ) {
        for (int x = posX; x < posX + sizeX; x++) {
            for (int y = posY; y < posY + sizeY; y++) {
                for (int z = posZ; z < posZ + sizeZ; z++) {
                    placeBlock(x, y, z, Material.AIR);
                }
            }
        }
        int numSpawners = 1 + random.nextInt(2);
        for (int i = 0; i < numSpawners; ++i) {
            int x = posX + random.nextInt(sizeX);
            int z = posZ + random.nextInt(sizeZ);
            placeSpawner(world.getBlockAt(x, posY, z));
        }

        for (int x = posX - 1; x <= posX + sizeX; x++) {
            for (int z = posZ - 1; z <= posZ + sizeZ; z++) {
                placeBlock(x, posY - 1, z, pickStone());
                placeBlock(x, posY + sizeY, z, pickStone());
            }
        }

        for (int y = posY - 1; y <= posY + sizeX; y++) {
            for (int z = posZ - 1; z <= posZ + sizeZ; z++) {
                placeBlock(posX - 1, y, z, pickStone());
                placeBlock(posX + sizeX, y, z, pickStone());
            }
        }

        for (int x = posX - 1; x <= posX + sizeX; x++) {
            for (int y = posY - 1; y <= posY + sizeY; y++) {
                placeBlock(x, y, posZ - 1, pickStone());
                placeBlock(x, y, posZ + sizeZ, pickStone());
            }
        }
    }
    
    private Material pickStone() {
        if (random.nextInt(6) == 0) {
            return Material.MOSSY_COBBLESTONE;
        } else {
            return Material.COBBLESTONE;
        }
    }

    private void placeSpawner(Block block) {
        String[] types = {
            "SKELETON", "ZOMBIE", "CREEPER", "SPIDER"
        };
        
        block.setType(Material.MOB_SPAWNER);
        ((CreatureSpawner) block.getState()).setCreatureTypeByName(types[random.nextInt(types.length)]);
    }

    private void placeBlock(int x, int y, int z, Material mat) {
        if (canPlaceBlock(x, y, z)) {
            world.getBlockAt(x, y, z).setType(mat);
        }
    }

    private boolean canPlaceBlock(int x, int y, int z) {
        switch (world.getBlockAt(x, y, z).getType()) {
            case AIR:
            case MOB_SPAWNER:
            case CHEST:
            case WATER:
            case STATIONARY_WATER:
            case LAVA:
            case STATIONARY_LAVA:
                return false;
            default:
                return true;
        }
    }

}
