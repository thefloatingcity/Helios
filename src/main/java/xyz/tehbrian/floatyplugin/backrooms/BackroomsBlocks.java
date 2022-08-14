package xyz.tehbrian.floatyplugin.backrooms;

import org.bukkit.Material;
import org.bukkit.block.data.type.Light;

/**
 * The block palette for the backrooms.
 */
public final class BackroomsBlocks {

  public static final Material LIGHT = Material.LIGHT;
  /**
   * Dimmed light data for use in light blocks.
   */
  public static final Light LIGHT_DATA = (Light) LIGHT.createBlockData();

  public static final Material WALL = Material.SMOOTH_SANDSTONE;
  /**
   * Everything that sandwiches the main area.
   */
  public static final Material SPACE = Material.INFESTED_STONE_BRICKS;
  public static final Material ACCENT = Material.CHISELED_SANDSTONE;

  static {
    LIGHT_DATA.setLevel(10);
  }

  private BackroomsBlocks() {
  }

}
