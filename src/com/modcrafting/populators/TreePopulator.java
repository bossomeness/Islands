package com.modcrafting.populators;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;

public class TreePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        int centerX = (chunk.getX() << 4) + random.nextInt(16);
        int centerZ = (chunk.getZ() << 4) + random.nextInt(16);

        byte data = 0;
        int chance = 0;
        int height = 4 + random.nextInt(3);
        int multiplier = 1;

        if (random.nextBoolean()) {
            data = 2;
            height = 5 + random.nextInt(3);
        }

        switch (world.getBlockAt(centerX, 0, centerZ).getBiome()) {
            case FOREST:
                chance = 375;
                multiplier = 10;
                break;
            case PLAINS:
                chance = 220;
                break;
            case SWAMPLAND:
                chance = 198;
                break;
            case TAIGA:
                chance = 300;
                data = 1;
                height = 8 + random.nextInt(3);
                multiplier = 3;
                break;
            default:
                chance = 295;
                data = 1;
                height = 7 + random.nextInt(3);
                break;
        }

        for (int i = 0; i < multiplier; i++) {
            centerX = (chunk.getX() << 4) + random.nextInt(16);
            centerZ = (chunk.getZ() << 4) + random.nextInt(16);
            if (random.nextInt(300) < chance) {
                int centerY = world.getHighestBlockYAt(centerX, centerZ) - 1;
                Block sourceBlock = world.getBlockAt(centerX, centerY, centerZ);
                
                if (sourceBlock.getType() == Material.GRASS) {
                    world.getBlockAt(centerX, centerY + height + 1, centerZ).setTypeIdAndData(18, data, true);
                    for (int j = 0; j < 4; j++) {
                        world.getBlockAt(centerX, centerY + height + 1 - j, centerZ - 1).setTypeIdAndData(18, data, true);
                        world.getBlockAt(centerX, centerY + height + 1 - j, centerZ + 1).setTypeIdAndData(18, data, true);
                        world.getBlockAt(centerX - 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(18, data, true);
                        world.getBlockAt(centerX + 1, centerY + height + 1 - j, centerZ).setTypeIdAndData(18, data, true);
                    }

                    if (random.nextBoolean()) {
                        world.getBlockAt(centerX + 1, centerY + height, centerZ + 1).setTypeIdAndData(18, data, true);
                    }
                    if (random.nextBoolean()) {
                        world.getBlockAt(centerX + 1, centerY + height, centerZ - 1).setTypeIdAndData(18, data, true);
                    }
                    if (random.nextBoolean()) {
                        world.getBlockAt(centerX - 1, centerY + height, centerZ + 1).setTypeIdAndData(18, data, true);
                    }
                    if (random.nextBoolean()) {
                        world.getBlockAt(centerX - 1, centerY + height, centerZ - 1).setTypeIdAndData(18, data, true);
                    }

                    world.getBlockAt(centerX + 1, centerY + height - 1, centerZ + 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX + 1, centerY + height - 1, centerZ - 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX - 1, centerY + height - 1, centerZ + 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX - 1, centerY + height - 1, centerZ - 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX + 1, centerY + height - 2, centerZ + 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX + 1, centerY + height - 2, centerZ - 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX - 1, centerY + height - 2, centerZ + 1).setTypeIdAndData(18, data, true);
                    world.getBlockAt(centerX - 1, centerY + height - 2, centerZ - 1).setTypeIdAndData(18, data, true);

                    for (int j = 0; j < 2; j++) {
                        for (int k = -2; k <= 2; k++) {
                            for (int l = -2; l <= 2; l++) {
                                world.getBlockAt(centerX + k, centerY + height - 1 - j, centerZ + l).setTypeIdAndData(18, data, true);
                            }
                        }
                    }

                    for (int j = 0; j < 2; j++) {
                        if (random.nextBoolean()) {
                            world.getBlockAt(centerX + 2, centerY + height - 1 - j, centerZ + 2).setTypeIdAndData(0, (byte) 0, true);
                        }
                        if (random.nextBoolean()) {
                            world.getBlockAt(centerX + 2, centerY + height - 1 - j, centerZ - 2).setTypeIdAndData(0, (byte) 0, true);
                        }
                        if (random.nextBoolean()) {
                            world.getBlockAt(centerX - 2, centerY + height - 1 - j, centerZ + 2).setTypeIdAndData(0, (byte) 0, true);
                        }
                        if (random.nextBoolean()) {
                            world.getBlockAt(centerX - 2, centerY + height - 1 - j, centerZ - 2).setTypeIdAndData(0, (byte) 0, true);
                        }
                    }
                    for (int y = 1; y <= height; y++) {
                        world.getBlockAt(centerX, centerY + y, centerZ).setTypeIdAndData(17, data, true);
                    }
                }
            }
        }
    }

}