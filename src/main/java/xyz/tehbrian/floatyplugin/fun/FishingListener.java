package xyz.tehbrian.floatyplugin.fun;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public final class FishingListener implements Listener {

  @EventHandler
  public void onFishInVoid(final PlayerFishEvent event) {
    if (event.getState() != PlayerFishEvent.State.REEL_IN
        || !(event.getHook().getLocation().getY() < -15)) {
      return;
    }

    final Player player = event.getPlayer();
    switch (player.getWorld().getEnvironment()) {
      case NETHER -> player.getInventory().addItem(PaperItemBuilder.ofType(Material.TROPICAL_FISH)
          .name(Component.text("Fiery Fish").color(NamedTextColor.RED))
          .loreList(
              Component.text("Likes to set things ablaze.").color(NamedTextColor.WHITE),
              Component.empty(),
              Component.text("Give this fish to a certain place").color(NamedTextColor.GRAY),
              Component.text("to acquire your reward.").color(NamedTextColor.GRAY)
          )
          .build());
      case THE_END -> player.getInventory().addItem(PaperItemBuilder.ofType(Material.SALMON)
          .name(Component.text("Void Fish").color(NamedTextColor.DARK_PURPLE))
          .loreList(
              Component.text("Has a mystical, purple-ish aura.").color(NamedTextColor.WHITE),
              Component.empty(),
              Component.text("Give this fish to a certain place").color(NamedTextColor.GRAY),
              Component.text("to acquire your reward.").color(NamedTextColor.GRAY)
          )
          .build());
      case NORMAL -> player.getInventory().addItem(PaperItemBuilder.ofType(Material.COD)
          .name(Component.text("Floaty Fish").color(NamedTextColor.AQUA))
          .loreList(
              Component.text("\"It's just.. floating there.\"").color(NamedTextColor.WHITE),
              Component.empty(),
              Component.text("Give this fish to a certain place").color(NamedTextColor.GRAY),
              Component.text("to acquire your reward.").color(NamedTextColor.GRAY)
          )
          .build());
      default -> player.getInventory().addItem(PaperItemBuilder.ofType(Material.PUFFERFISH)
          .name(Component.text("Odd Fish").color(NamedTextColor.GOLD))
          .loreList(
              Component.text("It doesn't look very appetizing.").color(NamedTextColor.WHITE),
              Component.empty(),
              Component.text("You can't do anything with this fish.").color(NamedTextColor.GRAY)
          ).build());
    }
  }

}
