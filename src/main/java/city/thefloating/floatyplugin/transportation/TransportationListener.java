package city.thefloating.floatyplugin.transportation;

import broccolai.corn.paper.item.PaperItemBuilder;
import broccolai.corn.paper.item.special.BookBuilder;
import broccolai.corn.paper.item.special.BundleBuilder;
import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.milk.MilkProvider;
import city.thefloating.floatyplugin.realm.Realm;
import city.thefloating.floatyplugin.soul.Charon;
import city.thefloating.floatyplugin.soul.Soul;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spongepowered.configurate.NodePath;

public final class TransportationListener implements Listener {

  private final LangConfig langConfig;
  private final FloatyPlugin plugin;
  private final Charon charon;

  @Inject
  public TransportationListener(
      final LangConfig langConfig,
      final FloatyPlugin plugin,
      final Charon charon
  ) {
    this.langConfig = langConfig;
    this.plugin = plugin;
    this.charon = charon;
  }

  /**
   * Prevents spectator mode.
   *
   * @param event
   */
  @EventHandler
  public void onModeToSpectator(final PlayerGameModeChangeEvent event) {
    final var player = event.getPlayer();
    if (event.getNewGameMode() == GameMode.SPECTATOR) {
      event.setCancelled(true);
      player.setGameMode(GameMode.ADVENTURE);
      player.setFireTicks(100);
      player.getWorld().strikeLightning(player.getLocation());
      player.sendMessage(this.langConfig.c(NodePath.path("no-spectator")));
    }
  }

  /**
   * Prevents ender pearls and chorus fruit teleportation except in the end.
   */
  @EventHandler
  public void onTeleport(final PlayerTeleportEvent event) {
    final var cause = event.getCause();
    if (cause != PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT
        && cause != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
      return;
    }

    // allow teleportation in the end.
    if (Realm.from(event.getPlayer().getWorld()) == Realm.END) {
      return;
    }

    event.setCancelled(true);
    event.getTo().getWorld().spawnParticle(Particle.SMOKE_NORMAL, event.getTo(), 200, 0.1, 0.1, 0.1, 0.1);
  }

  /**
   * Prevents vehicle usage in the nether.
   */
  @EventHandler
  public void onVehicleEnter(final VehicleEnterEvent event) {
    if (Realm.from(event.getEntered().getWorld()) != Realm.NETHER) {
      return;
    }

    if (event.getEntered() instanceof Player player) {
      final Vehicle vehicle = event.getVehicle();
      vehicle.getWorld().createExplosion(vehicle, 2, true, false);
      vehicle.remove();
      player.sendMessage(this.langConfig.c(NodePath.path("no-vehicle")));
    } else {
      event.setCancelled(true);
    }
  }

  /**
   * Prevents movement-enhancing potions in the nether.
   */
  @EventHandler
  public void onPotionEffect(final EntityPotionEffectEvent event) {
    if (!(event.getEntity() instanceof Player player)
        || Realm.from(player.getWorld()) != Realm.NETHER
        || event.getNewEffect() == null) {
      return;
    }

    final PotionEffectType type = event.getNewEffect().getType();
    if (type.equals(PotionEffectType.SPEED)
        || type.equals(PotionEffectType.JUMP)
        || type.equals(PotionEffectType.SLOW_FALLING)
        || type.equals(PotionEffectType.LEVITATION)) {
      event.setCancelled(true);
      player.setGameMode(GameMode.SURVIVAL);
      player.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10000, 100, false, true, true));
    }
  }

  /**
   * Prevents elytra usage outside the end.
   */
  @EventHandler
  public void onElytra(final EntityToggleGlideEvent event) {
    if (Realm.from(event.getEntity().getWorld()) == Realm.END
        || !event.isGliding()
        || !(event.getEntity() instanceof Player player)) {
      return;
    }

    player.setGliding(false);

    // play a cracking noise.
    player.playSound(player.getLocation(), Sound.ENTITY_TURTLE_EGG_CRACK, SoundCategory.MASTER, 100, 2);

    // remove their elytra.
    final PlayerInventory inventory = player.getInventory();
    if (inventory.getChestplate() != null && inventory.getChestplate().getType() == Material.ELYTRA) {
      inventory.setChestplate(new ItemStack(Material.AIR));
    }
  }

  /**
   * Prevents sprinting in the nether. Additionally, handles the nether watcher.
   */
  @EventHandler
  public void onSprint(final PlayerToggleSprintEvent event) {
    final Player player = event.getPlayer();
    if (Realm.from(player.getWorld()) != Realm.NETHER
        || !event.isSprinting()) {
      return;
    }

    player.setSprinting(false);

    // stop their sprint again in a few ticks to catch any glitchiness.
    player.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, () -> player.setSprinting(false), 5);

    // nether watcher time!
    final Soul soul = this.charon.getSoul(player);
    final var netherBlindnessCount = soul.netherBlindnessCount();
    switch (netherBlindnessCount) {
      case 0, 2, 5, 10 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "1")));
      case 20 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "2")));
      case 30 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "3")));
      case 40 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "4")));
      case 50 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "5")));
      case 60 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "6")));
      case 70 -> player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "7")));
      case 90 -> {
        player.sendMessage(this.langConfig.c(NodePath.path("no-sprint", "8")));

        final BundleBuilder bundleBuilder = BundleBuilder.ofBundle()
            .name(Component.text("Nether Watcher's Gift").color(NamedTextColor.RED))
            .loreList(Component.text("Maybe you should open it.").color(NamedTextColor.GRAY))
            .addItem(
                BookBuilder.ofType(Material.WRITTEN_BOOK)
                    .title(Component.text("A Letter"))
                    .author(Component.text("The Nether Watcher"))
                    .addPage(Component
                        .text("listen, i appreciate ya givin' me company,"
                            + " but holy frik, the whole point of the nether is *not* to sprint,"
                            + " yet you somehow managed to do it upwards of 50 times!??"
                            + " 'ave ya got somethin' wrong in the head??? love ya, but frik off")
                        .color(NamedTextColor.DARK_GRAY))
                    .build(),
                PaperItemBuilder.ofType(Material.SLIME_BALL)
                    .name(Component.text("Ball of Slime"))
                    .loreList(Component.text("It's uh.. a ball of slime.").color(NamedTextColor.GRAY))
                    .build(),
                PaperItemBuilder.ofType(Material.GOLD_NUGGET)
                    .name(Component.text("Gold Medal"))
                    .loreList(
                        Component.text("There's an inscription on").color(NamedTextColor.GRAY),
                        Component.text("the back. It says \"#1 Idiot\".").color(NamedTextColor.GRAY)
                    )
                    .build(),
                MilkProvider.splash()
            );

        player.getInventory().addItem(bundleBuilder.build());
      }
      default -> {
      }
    }

    soul.netherBlindnessCount(netherBlindnessCount + 1);

    // add some gnarly effects. blindness is important to prevent client-side sprint activation.
    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 4, true, false, false));
    player.addPotionEffect(new PotionEffect(
        PotionEffectType.BLINDNESS,
        PotionEffect.INFINITE_DURATION,
        1, true, false, false
    ));
    player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, SoundCategory.MASTER, 100, 0);
    player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, SoundCategory.MASTER, 100, 0);
    player.playSound(player.getLocation(), Sound.AMBIENT_WARPED_FOREST_MOOD, SoundCategory.MASTER, 100, 1);

  }

  /**
   * Remove any leftover blindness that the player might've had from the nether.
   */
  @EventHandler
  public void onWorldChange(final PlayerChangedWorldEvent event) {
    event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);
  }

}
