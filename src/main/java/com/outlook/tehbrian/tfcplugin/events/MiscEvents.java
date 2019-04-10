package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Flight;
import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Misc;
import com.outlook.tehbrian.tfcplugin.Piano;
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

public class MiscEvents implements Listener {

    private final Main main;

    public MiscEvents(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onToggleFlight(PlayerToggleFlightEvent event) {
        event.setCancelled(true);
        Flight.disableFlight(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage(Misc.formatConfig("msg_join", player.getDisplayName()));
        player.sendMessage(Misc.formatConfig(false, "msg_prefix_long"));

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
            Misc.oldPlayerJoin(player);
        } else {
            Misc.newPlayerJoin(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(Misc.formatConfig("msg_leave", event.getPlayer().getDisplayName()));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (event.getClickedInventory() != null) {
                if (event.getClickedInventory().getName().equals(main.getConfig().getString("piano_menu_inventory_name"))) {
                    if (event.isRightClick()) {
                        event.setCancelled(true);
                        Piano.play(player, event.getCurrentItem(), false);
                    }
                } else if (event.getClickedInventory().getName().equals(main.getConfig().getString("rules_inventory_name"))) {
                    event.setCancelled(true);
                    if (event.getSlot() == 8) {
                        player.sendMessage(Misc.formatConfig("msg_golden_rule"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHeldItemChange(PlayerItemHeldEvent event) {
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
                Misc.maximumWarp(player);
                player.getWorld().playSound(location, Sound.ENTITY_ENDERMEN_TELEPORT, SoundCategory.MASTER, 4, 1);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(Misc.colorString(event.getMessage()));
        }
        event.setFormat(Misc.colorString(main.getConfig().getString("chat_format")
                .replace("{prefix}", main.getVaultChat().getPlayerPrefix(player))
                .replace("{suffix}", main.getVaultChat().getPlayerSuffix(player))));
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.signcolor")) {
            String[] lines = event.getLines();
            for (int l = 0; l <= 3; l++) {
                event.setLine(l, ChatColor.translateAlternateColorCodes('&', lines[l]));
            }
        }
    }
}
