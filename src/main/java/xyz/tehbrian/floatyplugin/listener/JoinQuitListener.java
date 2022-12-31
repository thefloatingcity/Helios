package xyz.tehbrian.floatyplugin.listener;

import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.PlaytimeUtil;

import java.time.Duration;
import java.util.Calendar;

@SuppressWarnings({"unused"})
public final class JoinQuitListener implements Listener {

  private final FloatyPlugin plugin;
  private final LangConfig langConfig;

  @Inject
  public JoinQuitListener(
      final FloatyPlugin plugin,
      final LangConfig langConfig
  ) {
    this.plugin = plugin;
    this.langConfig = langConfig;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    player.sendMessage(this.langConfig.c(NodePath.path("banner")));

    if (player.hasPlayedBefore()) {
      event.joinMessage(this.langConfig.c(
          NodePath.path("join"),
          Placeholder.component("player", player.displayName())
      ));

      final Duration timeSinceLastPlayed = Duration.ofMillis(
          Calendar.getInstance().getTimeInMillis() - player.getLastPlayed());
      player.sendMessage(this.langConfig.c(
          NodePath.path("motd"),
          Placeholder.unparsed("last", PlaytimeUtil.fancifyTime(timeSinceLastPlayed))
      ));
    } else {
      event.joinMessage(this.langConfig.c(
          NodePath.path("join_new"),
          Placeholder.component("player", player.displayName())
      ));

      player.sendMessage(this.langConfig.c(
          NodePath.path("motd_new"),
          Placeholder.component("player", player.displayName())
      ));
    }

    player.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin,
        () -> {
          final Firework firework = player.getWorld().spawn(player.getLocation().add(0, 2, 0), Firework.class);
          final FireworkMeta fireworkMeta = firework.getFireworkMeta();

          if (player.hasPlayedBefore()) {
            fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL)
                .withColor(Color.WHITE, Color.BLUE, Color.GREEN)
                .withFade(Color.GREEN, Color.BLUE, Color.WHITE)
                .build());
            fireworkMeta.setPower(2);
          } else {
            fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(true)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.SILVER, Color.PURPLE, Color.TEAL)
                .withFade(Color.TEAL, Color.PURPLE, Color.SILVER)
                .build());
            fireworkMeta.setPower(3);
          }

          firework.setFireworkMeta(fireworkMeta);
        }, 2
    );
  }

  @EventHandler
  public void onQuit(final PlayerQuitEvent event) {
    event.quitMessage(this.langConfig.c(
        NodePath.path("leave"),
        Placeholder.component("player", event.getPlayer().displayName())
    ));
  }

}
