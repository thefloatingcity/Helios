package xyz.tehbrian.floatyplugin.world.backrooms;

import org.bukkit.Axis;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Lightable;
import org.bukkit.block.data.Orientable;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class BackroomsGenerator extends ChunkGenerator {

  private static void setColumn(
      final int chunkX, final int middleY, final int chunkZ,
      final Material material, final ChunkData chunkData
  ) {
    ChunkUtil.setRegionInclusive(
        chunkX, middleY - 2, chunkZ,
        chunkX, middleY + 2, chunkZ,
        material, chunkData
    );
  }

  private static void setColumn(
      final int chunkX, final int middleY, final int chunkZ,
      final BlockData blockData, final ChunkData chunkData
  ) {
    ChunkUtil.setRegionInclusive(
        chunkX, middleY - 2, chunkZ,
        chunkX, middleY + 2, chunkZ,
        blockData, chunkData
    );
  }

  private static void setWall(
      final int chunkX, final int middleY, final int chunkZ,
      final ChunkData chunkData
  ) {
    ChunkUtil.setRegionInclusive(
        chunkX, middleY - 2, chunkZ,
        chunkX, middleY + 2, chunkZ,
        Palette.WALL, chunkData
    );
    chunkData.setBlock(chunkX, middleY - 2, chunkZ, Palette.WALL_BASE);
  }

  private static void setShortWall(
      final int chunkX, final int middleY, final int chunkZ,
      final ChunkData chunkData
  ) {
    ChunkUtil.setRegionInclusive(
        chunkX, middleY - 2, chunkZ,
        chunkX, middleY + 1, chunkZ,
        Palette.WALL, chunkData
    );
    chunkData.setBlock(chunkX, middleY - 2, chunkZ, Palette.WALL_BASE);
  }

  private static void setLight(
      final int chunkX, final int middleY, final int chunkZ,
      final ChunkData chunkData
  ) {
    chunkData.setBlock(chunkX, middleY + 3, chunkZ, Palette.LAMP_GLASS);
    chunkData.setBlock(chunkX, middleY + 4, chunkZ, Palette.LAMP);
    chunkData.setBlock(chunkX, middleY + 4, chunkZ, Palette.LAMP_DATA);
  }

  /**
   * Calculates the number of sides of a block that are not taken by
   * {@link Material#AIR} or {@link Palette#SPACE}.
   * <p>
   * This does not take into account the bottom or top sides of the block.
   *
   * @param x         the x of the block
   * @param y         the y of the block
   * @param z         the z of the block
   * @param chunkData the chunk to search within
   * @return the number of sides that are air
   */
  private static @Range(from = 0, to = 4) Integer openSides(
      final int x,
      final int y,
      final int z,
      final ChunkGenerator.ChunkData chunkData
  ) {
    int result = 0;
    final var posX = chunkData.getBlockData(x + 1, y, z).getMaterial();
    if (isSpace(posX)) {
      result++;
    }
    final var negX = chunkData.getBlockData(x - 1, y, z).getMaterial();
    if (isSpace(negX)) {
      result++;
    }
    final var posZ = chunkData.getBlockData(x, y, z + 1).getMaterial();
    if (isSpace(posZ)) {
      result++;
    }
    final var negZ = chunkData.getBlockData(x, y, z - 1).getMaterial();
    if (isSpace(negZ)) {
      result++;
    }
    return result;
  }

  /**
   * @param material the material to check
   * @return whether the material is {@link Palette#SPACE} or {@link Material#AIR}
   */
  private static boolean isSpace(final Material material) {
    return material == Palette.SPACE || material == Material.AIR;
  }

  @Override
  public Location getFixedSpawnLocation(final @NotNull World world, final @NotNull Random random) {
    return new Location(world, 0.0D, ChunkUtil.middleY(world), 0.0D);
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
    ChunkUtil.setRegionInclusive(
        0, chunkData.getMinHeight(), 0,
        15, chunkData.getMaxHeight(), 15,
        Palette.SPACE, chunkData
    );

    // the main area will be placed at middle y.
    final int middleY = ChunkUtil.middleY(chunkData);

    // floor and ceiling.
    ChunkUtil.setRegionInclusive(0, middleY - 3, 0, 15, middleY - 3, 15, Palette.FLOOR, chunkData);
    ChunkUtil.setRegionInclusive(0, middleY - 3, 0, 15, middleY - 3, 15, Palette.FLOOR_DATA, chunkData);
    ChunkUtil.setRegionInclusive(0, middleY + 3, 0, 15, middleY + 3, 15, Palette.CEILING, chunkData);

    // lights.
    setLight(3, middleY, 3, chunkData);
    setLight(3, middleY, 11, chunkData);
    setLight(11, middleY, 3, chunkData);
    setLight(11, middleY, 11, chunkData);

    setLight(7, middleY, 7, chunkData);
    setLight(7, middleY, 15, chunkData);
    setLight(15, middleY, 7, chunkData);
    setLight(15, middleY, 15, chunkData);

    // air area.
    ChunkUtil.setRegionInclusive(0, middleY - 2, 0, 15, middleY + 2, 15, Material.AIR, chunkData);

    // walls.
    final int minLengthToChangeDirections = 3;
    for (int iteration = 0; iteration < 9; iteration++) {
      final int maxLength = random.nextInt(5, 26);
      final boolean isShort = random.nextFloat() < 0.15F; // 15% chance.

      int length = 0; // the overall length of the line.
      int directionLength = 0; // the length since the last direction change.
      Direction direction = randomDirection();
      int x = random.nextInt(0, 16);
      int z = random.nextInt(0, 16);
      while (!hasHitWall(x, z) && length < maxLength) {
        if (isShort) {
          setShortWall(x, middleY, z, chunkData);
        } else {
          setWall(x, middleY, z, chunkData);
        }

        switch (direction) {
          case NORTH -> z--;
          case SOUTH -> z++;
          case EAST -> x++;
          case WEST -> x--;
          default -> throw new IllegalArgumentException("wtf");
        }

        if (directionLength >= minLengthToChangeDirections && random.nextFloat() < 0.20F) { // 20% chance to pick a new direction.
          direction = randomDirectionNot(direction);
        }

        length += 1;
        directionLength += 1;
      }
    }
  }

  private static boolean hasHitWall(final int x, final int z) {
    return x < 0 || x > 15 || z < 0 || z > 15;
  }

  private static Direction randomDirection() {
    return Direction.values()[new Random().nextInt(Direction.values().length)];
  }

  /**
   * Gives a new random direction that is not the current direction.
   *
   * @param current the banned direction
   * @return a new random direction
   */
  private static Direction randomDirectionNot(final Direction current) {
    final List<Direction> available = Arrays.stream(Direction.values())
        .filter(d -> d != current)
        .toList();
    assert available.size() == 3;
    return available.get(new Random().nextInt(available.size()));
  }

  /**
   * The block palette for the backrooms.
   */
  private static final class Palette {

    public static final Material WALL = Material.SMOOTH_SANDSTONE;
    public static final Material WALL_BASE = Material.CUT_SANDSTONE;

    public static final Material SPACE = Material.BLACK_CONCRETE;

    public static final Material CEILING = Material.WHITE_TERRACOTTA;
    public static final Material FLOOR = Material.STRIPPED_BIRCH_LOG;
    public static final Orientable FLOOR_DATA = (Orientable) FLOOR.createBlockData();

    public static final Material LAMP = Material.REDSTONE_LAMP;
    public static final Lightable LAMP_DATA = (Lightable) LAMP.createBlockData();
    public static final Material LAMP_GLASS = Material.LIGHT_GRAY_STAINED_GLASS;

    static {
      FLOOR_DATA.setAxis(Axis.X);
      LAMP_DATA.setLit(true);
    }

    private Palette() {
    }

  }

}
