package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;

import java.util.Objects;

@SuppressWarnings("unused")
public class OptionsListener implements Listener {

    private final TFCPlugin main;

    public OptionsListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_entity_explode")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityExplode(EntityDamageEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_entity_damage_by_entity_explode")) {
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_block_explode")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByBlockExplode(EntityDamageEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_entity_damage_by_block_explode")) {
            if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_leaves_decay")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFarmlandTrample(PlayerInteractEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_farmland_trample")) {
            if (event.getAction() == Action.PHYSICAL) {
                if (Objects.requireNonNull(event.getClickedBlock()).getType() == Material.FARMLAND) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDragonEggTeleport(BlockFromToEvent event) {
        if (this.main.getConfig().getBoolean("options.disable_dragon_egg_teleport")) {
            if (event.getBlock().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
    }
}
