package city.thefloating.helios.server;

import city.thefloating.helios.DurationFormat;
import city.thefloating.helios.Helios;
import city.thefloating.helios.config.ConfigConfig;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.realm.Realm;
import city.thefloating.helios.realm.WorldService;
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
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.spongepowered.configurate.NodePath;

import java.net.URI;
import java.time.Duration;
import java.util.Calendar;

import static net.kyori.adventure.resource.ResourcePackInfo.resourcePackInfo;
import static net.kyori.adventure.resource.ResourcePackRequest.resourcePackRequest;

public final class JoinQuitListener implements Listener {

  private final Helios plugin;
  private final LangConfig langConfig;
  private final ConfigConfig configConfig;
  private final WorldService worldService;

  @Inject
  public JoinQuitListener(
      final Helios plugin,
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
  public void onPack(final PlayerResourcePackStatusEvent event) {
    if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
      event.getPlayer().kick(this.langConfig.c(NodePath.path("resource-pack", "decline-kick")));
    } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
      event.getPlayer().kick(this.langConfig.c(NodePath.path("resource-pack", "fail-kick")));
    }
  }

  @EventHandler
  public void onJoin(final PlayerJoinEvent event) {
    final Player player = event.getPlayer();

    player.sendResourcePacks(resourcePackRequest()
        .required(true)
        .packs(resourcePackInfo()
            .uri(URI.create(this.configConfig.data().resourcePackUrl()))
            .hash(this.configConfig.data().resourcePackHash())
        )
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
          Placeholder.unparsed("last", DurationFormat.fancifyTime(sinceLastPlayed))
      ));
    } else {
      if (this.configConfig.data().madlandsEnabled()) {
        event.getPlayer().teleport(this.worldService.ornateSpawn(Realm.MADLANDS));
      } else {
        event.getPlayer().teleport(this.worldService.ornateSpawn(Realm.OVERWORLD));
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

    player.getServer().getScheduler().runTaskLater(this.plugin,
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
