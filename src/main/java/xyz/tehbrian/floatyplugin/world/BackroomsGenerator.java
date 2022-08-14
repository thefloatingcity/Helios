package xyz.tehbrian.floatyplugin.world;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Light;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Random;

public final class BackroomsGenerator extends ChunkGenerator {

  public static final Material LIGHT = Material.LIGHT;
  public static final Material WALL = Material.SMOOTH_SANDSTONE;
  public static final Material SPACE = Material.STONE_BRICKS;

  private static int middleY(final int minY, final int maxY) {
    return (minY + maxY) / 2;
  }

  private static @Range(from = 0, to = 4) Integer openSides(
      final int x,
      final int y,
      final int z,
      final @NotNull ChunkData chunkData
  ) {
    int result = 0;
    final var posX = chunkData.getBlockData(x + 1, y, z).getMaterial();
    if (posX != WALL) {
      result++;
    }
    final var negX = chunkData.getBlockData(x - 1, y, z).getMaterial();
    if (negX != WALL) {
      result++;
    }
    final var posZ = chunkData.getBlockData(x, y, z + 1).getMaterial();
    if (posZ != WALL) {
      result++;
    }
    final var negZ = chunkData.getBlockData(x, y, z - 1).getMaterial();
    if (negZ != WALL) {
      result++;
    }
    return result;
  }

  private static void setColumn(
      final ChunkData chunkData,
      final int chunkX, final int middleY, final int chunkZ, final Material material
  ) {
    // wondering why the positions look so funny?
    // remember: ChunkData#setRegion max positions are EXCLUSIVE.
    chunkData.setRegion(
        chunkX, middleY - 1, chunkZ,
        chunkX + 1, middleY + 2, chunkZ + 1,
        material
    );
  }

  private static void setColumn(
      final ChunkData chunkData,
      final int chunkX, final int middleY, final int chunkZ, final BlockData blockData
  ) {
    chunkData.setRegion(
        chunkX, middleY - 1, chunkZ,
        chunkX + 1, middleY + 2, chunkZ + 1,
        blockData
    );
  }

  @Override
  public Location getFixedSpawnLocation(final @NotNull World world, final @NotNull Random random) {
    return new Location(world, 0.0D, middleY(world.getMinHeight(), world.getMaxHeight()), 0.0D);
  }

  @Override
  public void generateNoise(
      final @NotNull WorldInfo worldInfo,
      final @NotNull Random random,
      final int chunkX,
      final int chunkZ,
      final @NotNull ChunkData chunkData
  ) {
    // entire world other than the main area is solid.
    chunkData.setRegion(
        0, chunkData.getMinHeight(), 0,
        16, chunkData.getMaxHeight(), 16,
        SPACE
    );

    // dimmed light data for use in light blocks.
    final Light lightData = ((Light) LIGHT.createBlockData());
    lightData.setLevel(10);

    // the main area will be placed at middle y.
    final int middleY = middleY(chunkData.getMinHeight(), chunkData.getMaxHeight());

    for (int x = 0; x < 16; x++) {
      for (int z = 0; z < 16; z++) {
        // open sides won't ever be less than 2 because of our line-by-line
        // approach to looping over positions.
        final int openSides = openSides(x, middleY, z, chunkData);
        final boolean generateWall = (openSides == 4 && random.nextFloat() < 0.1F)
            || (openSides == 3 && random.nextFloat() < 0.7F);

        if (generateWall) {
          // wall column.
          setColumn(chunkData, x, middleY, z, WALL);

          // add variety to the wall.
          if (random.nextFloat() < 0.01F) {
            chunkData.setBlock(x, middleY + 1, z, Material.CHISELED_SANDSTONE);
          }
          if (random.nextFloat() < 0.01F) {
            chunkData.setBlock(x, middleY, z, Material.CHISELED_SANDSTONE);
          }
          if (random.nextFloat() < 0.01F) {
            chunkData.setBlock(x, middleY - 1, z, Material.CHISELED_SANDSTONE);
          }
        } else {
          // empty column of light blocks.
          setColumn(chunkData, x, middleY, z, LIGHT);
          setColumn(chunkData, x, middleY, z, lightData);
        }
      }
    }
  }

}
