package xyz.tehbrian.floatyplugin.world;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public final class VoidGenerator extends ChunkGenerator {

    public Location getFixedSpawnLocation(@NotNull final World var1, @NotNull final Random var2) {
        return new Location(var1, 0.0D, 64.0D, 0.0D);
    }

}
