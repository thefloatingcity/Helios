package city.thefloating.floatyplugin.realm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public final class VoidGenerator extends ChunkGenerator {

  private final MixedBagBiomeProvider mixedBagBiomeProvider = new MixedBagBiomeProvider();

  @Override
  public Location getFixedSpawnLocation(final @NotNull World world, final @NotNull Random random) {
    return new Location(world, 0.5D, 65.0D, 0.5D);
  }

  @Override
  public BiomeProvider getDefaultBiomeProvider(@NotNull final WorldInfo worldInfo) {
    return this.mixedBagBiomeProvider;
  }

  public static final class MixedBagBiomeProvider extends BiomeProvider {

    // excluded Biome enum values:
    // THE_VOID
    // CUSTOM

    private static final List<Biome> WHITE_BIOMES = List.of(
        Biome.OCEAN,
        Biome.PLAINS,
        Biome.DESERT,
        Biome.WINDSWEPT_HILLS,
        Biome.FOREST,
        Biome.TAIGA,
        Biome.SWAMP,
        Biome.MANGROVE_SWAMP,
        Biome.RIVER,
        Biome.FROZEN_OCEAN,
        Biome.FROZEN_RIVER,
        Biome.SNOWY_PLAINS,
        Biome.MUSHROOM_FIELDS,
        Biome.BEACH,
        Biome.JUNGLE,
        Biome.SPARSE_JUNGLE,
        Biome.DEEP_OCEAN,
        Biome.STONY_SHORE,
        Biome.SNOWY_BEACH,
        Biome.BIRCH_FOREST,
        Biome.DARK_FOREST,
        Biome.SNOWY_TAIGA,
        Biome.OLD_GROWTH_PINE_TAIGA,
        Biome.WINDSWEPT_FOREST,
        Biome.SAVANNA,
        Biome.SAVANNA_PLATEAU,
        Biome.BADLANDS,
        Biome.WOODED_BADLANDS,
        Biome.WARM_OCEAN,
        Biome.LUKEWARM_OCEAN,
        Biome.COLD_OCEAN,
        Biome.DEEP_LUKEWARM_OCEAN,
        Biome.DEEP_COLD_OCEAN,
        Biome.DEEP_FROZEN_OCEAN,
        Biome.SUNFLOWER_PLAINS,
        Biome.WINDSWEPT_GRAVELLY_HILLS,
        Biome.FLOWER_FOREST,
        Biome.ICE_SPIKES,
        Biome.OLD_GROWTH_BIRCH_FOREST,
        Biome.OLD_GROWTH_SPRUCE_TAIGA,
        Biome.WINDSWEPT_SAVANNA,
        Biome.ERODED_BADLANDS,
        Biome.BAMBOO_JUNGLE,
        Biome.DRIPSTONE_CAVES,
        Biome.LUSH_CAVES,
        Biome.DEEP_DARK,
        Biome.MEADOW,
        Biome.GROVE,
        Biome.SNOWY_SLOPES,
        Biome.FROZEN_PEAKS,
        Biome.JAGGED_PEAKS,
        Biome.STONY_PEAKS,
        Biome.CHERRY_GROVE
    );

    private static final List<Biome> RED_BIOMES = List.of(
        Biome.NETHER_WASTES,
        Biome.SOUL_SAND_VALLEY,
        Biome.CRIMSON_FOREST,
        Biome.WARPED_FOREST,
        Biome.BASALT_DELTAS

    );

    private static final List<Biome> BLACK_BIOMES = List.of(
        Biome.THE_END,
        Biome.SMALL_END_ISLANDS,
        Biome.END_MIDLANDS,
        Biome.END_HIGHLANDS,
        Biome.END_BARRENS
    );

    private static final int BIOME_CHUNK_SIZE = 96;

    @Override
    public @NotNull Biome getBiome(@NotNull final WorldInfo worldInfo, final int x, final int y, final int z) {
      final long seed = Long.parseLong(new StringBuilder()
          // depend on signs of x and z because we take absolute values below.
          .append(x >= 0 ? 1 : 0)
          .append(z >= 0 ? 1 : 0)
          // concatenate x and z to avoid additive commutativity. note the integer (floored) division.
          .append(Math.abs(x) / BIOME_CHUNK_SIZE)
          .append(Math.abs(z) / BIOME_CHUNK_SIZE)
          .toString()
      );
      final Random random = new Random(seed);

      final List<Biome> biomes = this.getBiomes(worldInfo);
      return biomes.get(random.nextInt(biomes.size()));
    }

    @Override
    public @NotNull List<Biome> getBiomes(@NotNull final WorldInfo worldInfo) {
      return switch (Habitat.from(worldInfo.getEnvironment())) {
        case WHITE -> WHITE_BIOMES;
        case RED -> RED_BIOMES;
        case BLACK -> BLACK_BIOMES;
      };
    }

  }

}
