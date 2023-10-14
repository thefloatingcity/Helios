package city.thefloating.floatyplugin.server;

import city.thefloating.floatyplugin.DurationFormatter;
import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.config.ConfigConfig;
import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.realm.Realm;
import city.thefloating.floatyplugin.realm.WorldService;
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

import java.time.Duration;
import java.util.Calendar;

public final class JoinQuitListener implements Listener {

  private final FloatyPlugin plugin;
  private final LangConfig langConfig;
  private final ConfigConfig configConfig;
  private final WorldService worldService;

  @Inject
  public JoinQuitListener(
      final FloatyPlugin plugin,
      final LangConfig langConfig,
      final ConfigConfig configConfig,
      final WorldService worldService
  ) {
    this.plugin = plugin;
    this.langConfig = langConfig;
    this.configConfig = configConfig;
    this.worldService = worldService;
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    player.setResourcePack(
        this.configConfig.data().resourcePackUrl(),
        this.configConfig.data().resourcePackHash()
    );

    player.sendMessage(this.langConfig.c(NodePath.path("banner")));

    if (player.hasPlayedBefore()) {
      event.joinMessage(this.langConfig.c(
          NodePath.path("join"),
          Placeholder.component("player", player.displayName())
      ));

      final Duration sinceLastPlayed = Duration.ofMillis(
          Calendar.getInstance().getTimeInMillis() - player.getLastPlayed()
      );
      player.sendMessage(this.langConfig.c(
          NodePath.path("motd"),
          Placeholder.unparsed("last", DurationFormatter.fancifyTime(sinceLastPlayed))
      ));
    } else {
      if (this.configConfig.data().madlandsEnabled()) {
        event.getPlayer().teleport(this.worldService.getSpawnPoint(Realm.MADLANDS));
      } else {
        event.getPlayer().teleport(this.worldService.getSpawnPoint(Realm.OVERWORLD));
      }

      event.joinMessage(this.langConfig.c(
          NodePath.path("join-new"),
          Placeholder.component("player", player.displayName())
      ));

      player.sendMessage(this.langConfig.c(
          NodePath.path("motd-new"),
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
