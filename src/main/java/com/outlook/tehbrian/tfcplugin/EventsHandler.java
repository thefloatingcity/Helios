package com.outlook.tehbrian.tfcplugin;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class EventsHandler implements Listener {
    private final Main plugin;

    public EventsHandler(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHangingBreak(HangingBreakByEntityEvent event) {
        if (event.getRemover().getType() == EntityType.PLAYER) {
            Player player = (Player) event.getRemover();
            if (!player.hasPermission("tfcplugin.build")) {
                player.sendMessage(plugin.formatChat("msg_no_build"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player) {
            Player player = (Player) event.getPlayer();
            if (!player.hasPermission("tfcplugin.build")) {
                player.sendMessage(plugin.formatChat("msg_no_build"));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(PlayerAttemptPickupItemEvent event) {
        if (!event.getPlayer().hasPermission("tfcplugin.build")) {
            event.getPlayer().sendMessage(plugin.formatChat("msg_no_build"));
            event.setCancelled(true);
        }
    }

    public void oldPlayer(Player player) {
        Date date = new Date();
        long secondsSinceLastPlayed = TimeUnit.MILLISECONDS.toSeconds(date.getTime() - player.getLastPlayed());
        if (secondsSinceLastPlayed >= 86400) {
            long daysSinceLastPlayed = TimeUnit.SECONDS.toDays(secondsSinceLastPlayed);
            player.sendMessage(plugin.formatChat("msg_motd", daysSinceLastPlayed, "days"));
        } else if (secondsSinceLastPlayed >= 3600) {
            long hoursSinceLastPlayed = TimeUnit.SECONDS.toHours(secondsSinceLastPlayed);
            player.sendMessage(plugin.formatChat("msg_motd", hoursSinceLastPlayed, "hours"));
        } else if (secondsSinceLastPlayed >= 60) {
            long minutesSinceLastPlayed = TimeUnit.SECONDS.toMinutes(secondsSinceLastPlayed);
            player.sendMessage(plugin.formatChat("msg_motd", minutesSinceLastPlayed, "minutes"));
        } else {
            player.sendMessage(plugin.formatChat("msg_motd", secondsSinceLastPlayed, "seconds"));
        }

        Firework f = player.getWorld().spawn(player.getLocation(), Firework.class);
        FireworkMeta fm = f.getFireworkMeta();
        fm.addEffect(FireworkEffect.builder()
                .flicker(true)
                .trail(false)
                .with(FireworkEffect.Type.BALL_LARGE)
                .withColor(Color.WHITE, Color.BLUE, Color.GREEN)
                .withFade(Color.GREEN, Color.BLUE, Color.WHITE)
                .build());
        fm.setPower(2);
        f.setFireworkMeta(fm);
    }

    public void newPlayer(Player player) {
        player.sendMessage(plugin.formatChat("msg_welcome", player.getName()));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(plugin.formatChat("msg_join", player.getName()));
        player.sendMessage(plugin.formatChat(false, "msg_long_prefix"));
        plugin.disableFlight(player);
        if (player.hasPlayedBefore()) {
            oldPlayer(player);
        } else {
            newPlayer(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(plugin.formatChat("msg_leave", event.getPlayer().getName()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().equals("Rules | The Floating City")) {
            if (event.getRawSlot() == 8) {
                if (event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(plugin.formatChat("golden_rule"));
                }
            }
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        plugin.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        plugin.disableFlight(event.getPlayer());
    }

    public void maximumWarp(Player player) {
        if (player.getFallDistance() >= 1000) {
            player.sendMessage(plugin.formatChat("msg_warp_max"));
            player.teleport(plugin.getSpawn());
            player.getWorld().strikeLightningEffect(plugin.getSpawn());
            player.playSound(plugin.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
            player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, plugin.getSpawn(), 1);
        } else if (player.getFallDistance() >= 750) {
            player.sendMessage(plugin.formatChat("msg_warp_second"));
        } else if (player.getFallDistance() >= 500) {
            player.sendMessage(plugin.formatChat("msg_warp_first"));
        }
    }

    @EventHandler
    public void voidLoop(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(0);
            Location location = event.getEntity().getLocation();
            location.setY(360);
            event.getEntity().teleport(location);
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                maximumWarp(player);
                player.playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 100000, 1);
            }
        }
    }

    @EventHandler
    public void chatColor(AsyncPlayerChatEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(ChatColor.translateAlternateColorCodes('&', event.getMessage()));
        }
    }

    @EventHandler
    public void signColor(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.signcolor")) {
            String[] lines = event.getLines();
            for (int l = 0; l <= 3; l++) {
                event.setLine(l, ChatColor.translateAlternateColorCodes('&', lines[l]));
            }
        }
    }

    @EventHandler
    public void playPiano(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerPlaysPiano(player) && player.hasPermission("tfcplugin.piano")) {
            if (event.getNewSlot() == 0) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.5F);
            } else if (event.getNewSlot() == 1) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.561231F);
            } else if (event.getNewSlot() == 2) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.629961F);
            } else if (event.getNewSlot() == 3) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.667420F);
            } else if (event.getNewSlot() == 4) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.749154F);
            } else if (event.getNewSlot() == 5) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.840896F);
            } else if (event.getNewSlot() == 6) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 0.943874F);
            } else if (event.getNewSlot() == 7) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 1F);
            } else if (event.getNewSlot() == 8) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_HARP, 10, 1.122462F);
            }
        }
    }
}
