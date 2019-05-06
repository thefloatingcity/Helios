package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Piano;
import com.outlook.tehbrian.tfcplugin.utils.DatabaseUtils;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
import com.outlook.tehbrian.tfcplugin.utils.TextUtils;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Calendar;

@SuppressWarnings("unused")
public class MiscEvents implements Listener {

    private final Main main;

    public MiscEvents(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(TextUtils.formatC("none", "tfc_banner"));

        Flight.disableFlight(player);

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

        if (player.hasPlayedBefore()) {
            event.setJoinMessage(TextUtils.formatC("none", "msg_join", player.getDisplayName()));

            long millisSinceLastPlayed = Calendar.getInstance().getTimeInMillis() - player.getLastPlayed();
            if (millisSinceLastPlayed >= 86400000) {
                player.sendMessage(TextUtils.format("msg_motd", Math.floor((millisSinceLastPlayed / 86400000d) * 100) / 100, "days"));
            } else if (millisSinceLastPlayed >= 3600000) {
                player.sendMessage(TextUtils.format("msg_motd", Math.floor((millisSinceLastPlayed / 3600000d) * 100) / 100, "hours"));
            } else if (millisSinceLastPlayed >= 60000) {
                player.sendMessage(TextUtils.format("msg_motd", Math.floor((millisSinceLastPlayed / 60000d) * 100) / 100, "minutes"));
            } else if (millisSinceLastPlayed >= 1000) {
                player.sendMessage(TextUtils.format("msg_motd", Math.floor((millisSinceLastPlayed / 1000d) * 100) / 100, "seconds"));
            } else {
                player.sendMessage(TextUtils.format("msg_motd", Math.floor((millisSinceLastPlayed) * 100) / 100, "milliseconds"));
            }
        } else {
            event.setJoinMessage(TextUtils.formatC("none", "msg_join_new", player.getDisplayName()));

            player.sendMessage(TextUtils.format("msg_motd_new", player.getName()));
        }
        DatabaseUtils.updatePlayer(player, 151);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(TextUtils.formatC("none", "msg_leave", event.getPlayer().getDisplayName()));
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        event.setCancelled(true);
        Flight.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getName().equals(main.getConfig().getString("piano_notes_inventory_name"))) {
                    if (event.isRightClick()) {
                        event.setCancelled(true);
                        Piano.play(player, event.getCurrentItem(), false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemHeld(PlayerItemHeldEvent event) {
        Piano.play(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()), true);
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            event.setDamage(0);
            Location location = event.getEntity().getLocation();
            location.setY(500);
            event.getEntity().teleport(location);
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (player.getFallDistance() >= 1500) {
                    player.sendMessage(TextUtils.format("msg_warp_max"));
                    player.teleport(MiscUtils.getSpawn());
                    player.getWorld().strikeLightningEffect(MiscUtils.getSpawn());
                    player.getWorld().playSound(MiscUtils.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 3, 1);
                    player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, MiscUtils.getSpawn(), 1);
                } else if (player.getFallDistance() >= 1000) {
                    player.sendMessage(TextUtils.format("msg_warp_second"));
                } else if (player.getFallDistance() >= 500) {
                    player.sendMessage(TextUtils.format("msg_warp_first"));
                }
                player.getWorld().playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 4, 1);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(TextUtils.color(event.getMessage()));
        }
        event.setFormat(TextUtils.color(main.getConfig().getString("chat_format")
                .replace("{prefix}", main.getVaultChat().getPlayerPrefix(player))
                .replace("{suffix}", main.getVaultChat().getPlayerSuffix(player))));
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.signcolor")) {
            String[] lines = event.getLines();
            for (int l = 0; l < 4; l++) {
                event.setLine(l, ChatColor.translateAlternateColorCodes('&', lines[l]));
            }
        }
    }
}
