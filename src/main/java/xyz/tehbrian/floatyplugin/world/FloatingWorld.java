package xyz.tehbrian.floatyplugin.world;

import org.bukkit.World;
import org.checkerframework.checker.nullness.qual.NonNull;

public enum FloatingWorld {
    MADLANDS("madlands", World.Environment.NORMAL),
    OVERWORLD("overworld", World.Environment.NORMAL),
    NETHER("nether", World.Environment.NETHER),
    END("end", World.Environment.THE_END);

    private final String bukkitName;
    private final World.Environment environment;

    FloatingWorld(final String bukkitName, final World.Environment environment) {
        this.bukkitName = bukkitName;
        this.environment = environment;
    }

    public @NonNull String bukkitName() {
        return this.bukkitName;
    }

    public World.@NonNull Environment environment() {
        return this.environment;
    }
}
