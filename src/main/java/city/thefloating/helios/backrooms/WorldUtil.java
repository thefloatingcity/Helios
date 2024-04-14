package city.thefloating.helios.backrooms;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator;

/**
 * Various utilities to help with generating worlds.
 */
public final class WorldUtil {

  private WorldUtil() {
  }

  private static int middleY(final int minY, final int maxY) {
    return (minY + maxY) / 2;
  }

  public static int middleY(final World world) {
    return middleY(world.getMinHeight(), world.getMaxHeight());
  }

  public static int middleY(final ChunkGenerator.ChunkData chunkData) {
    return middleY(chunkData.getMinHeight(), chunkData.getMaxHeight());
  }

  /**
   * Equivalent to {@link ChunkGenerator.ChunkData#setRegion(int, int, int, int, int, int, Material)}
   * except uses only inclusive min/max values rather than mixing inclusive and exclusive.
   *
   * @param xMin      minimum x location (inclusive) in the chunk to set
   * @param yMin      minimum y location (inclusive) in the chunk to set
   * @param zMin      minimum z location (inclusive) in the chunk to set
   * @param xMax      maximum x location (inclusive) in the chunk to set
   * @param yMax      maximum y location (inclusive) in the chunk to set
   * @param zMax      maximum z location (inclusive) in the chunk to set
   * @param material  the type to set the blocks to
   * @param chunkData the chunk to set the blocks in
   */
  public static void setRegionInclusive(
      final int xMin, final int yMin, final int zMin,
      final int xMax, final int yMax, final int zMax,
      final Material material, final ChunkGenerator.ChunkData chunkData
  ) {
    chunkData.setRegion(xMin, yMin, zMin, xMax + 1, yMax + 1, zMax + 1, material);
  }

  /**
   * Equivalent to {@link ChunkGenerator.ChunkData#setRegion(int, int, int, int, int, int, BlockData)}
   * except uses only inclusive min/max values rather than mixing inclusive and exclusive.
   *
   * @param xMin      minimum x location (inclusive) in the chunk to set
   * @param yMin      minimum y location (inclusive) in the chunk to set
   * @param zMin      minimum z location (inclusive) in the chunk to set
   * @param xMax      maximum x location (inclusive) in the chunk to set
   * @param yMax      maximum y location (inclusive) in the chunk to set
   * @param zMax      maximum z location (inclusive) in the chunk to set
   * @param blockData the data to set the blocks to
   * @param chunkData the chunk to set the blocks in
   */
  public static void setRegionInclusive(
      final int xMin, final int yMin, final int zMin,
      final int xMax, final int yMax, final int zMax,
      final BlockData blockData, final ChunkGenerator.ChunkData chunkData
  ) {
    chunkData.setRegion(xMin, yMin, zMin, xMax + 1, yMax + 1, zMax + 1, blockData);
  }

}
