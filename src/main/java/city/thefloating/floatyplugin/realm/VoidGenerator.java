package city.thefloating.floatyplugin.realm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class VoidGenerator extends ChunkGenerator {

  @Override
  public Location getFixedSpawnLocation(final @NotNull World world, final @NotNull Random random) {
    return new Location(world, 0.5D, 65.0D, 0.5D);
  }

}
