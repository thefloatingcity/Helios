package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

@SuppressWarnings({"unused"})
public class BuildingListener implements Listener {

    private final TFCPlugin main;

    public BuildingListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (main.getConfig().getBoolean("options.explosions_disabled")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) {
            if (main.getConfig().getBoolean("options.explosions_damage_disabled")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (main.getConfig().getBoolean("options.leaves_decay_disabled")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onCropsTrample(PlayerInteractEvent event) {
        if (main.getConfig().getBoolean("options.crop_trampling_disabled")) {
            if (event.getAction() == Action.PHYSICAL) {
                if (event.getClickedBlock().getType() == Material.FARMLAND) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDragonEggTeleport(BlockFromToEvent event) {
        if (main.getConfig().getBoolean("options.dragon_egg_teleporting_disabled")) {
            if (event.getBlock().getType() == Material.DRAGON_EGG) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        if (event.getPlayer().hasPermission("tfcplugin.signcolor")) {
            String[] lines = event.getLines();
            for (int l = 0; l < lines.length; l++) {
                event.setLine(l, MiscUtils.color(lines[l]));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (event.getPlayer().isSneaking()) return;

        BlockState blockState = event.getClickedBlock().getState();
        if (!(blockState instanceof Sign)) return;
        if (!(Tag.SIGNS.isTagged(event.getPlayer().getInventory().getItemInMainHand().getType()))) return;

        Bukkit.getScheduler().runTask(main, () -> {
            Sign sign = (Sign) blockState;
            String[] lines = sign.getLines();
            for (int l = 0; l < lines.length; l++) {
                sign.setLine(l, lines[l].replace('§', '&'));
            }
            sign.update();
            event.getPlayer().openSign(sign);
        });
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onIronTrapDoorInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (event.getPlayer().isSneaking()) return;
        if (event.getClickedBlock().getType() != Material.IRON_TRAPDOOR) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) return;

        Bukkit.getScheduler().runTask(main, () -> {
            Block block = event.getClickedBlock();

            TrapDoor trapDoor = (TrapDoor) block.getBlockData();

            if (trapDoor.isOpen()) {
                trapDoor.setOpen(false);
            } else {
                trapDoor.setOpen(true);
            }

            block.setBlockData(trapDoor);
        });
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onGlazedTerracottaInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (!event.getPlayer().isSneaking()) return;
        if (!event.getClickedBlock().getType().name().toLowerCase().contains("glazed")) return;
        if (event.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) return;

        Bukkit.getScheduler().runTask(main, () -> {
            Block block = event.getClickedBlock();
            Directional directional = (Directional) block.getBlockData();

            switch (directional.getFacing()) {
                case NORTH:
                    directional.setFacing(BlockFace.EAST);
                    break;
                case EAST:
                    directional.setFacing(BlockFace.SOUTH);
                    break;
                case SOUTH:
                    directional.setFacing(BlockFace.WEST);
                    break;
                case WEST:
                    directional.setFacing(BlockFace.NORTH);
                    break;
            }

            block.setBlockData(directional);
        });
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onSlabBreak(BlockBreakEvent event) {
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) return;
        if (!Tag.SLABS.isTagged(event.getPlayer().getInventory().getItemInMainHand().getType())) return;
        if (!Tag.SLABS.isTagged(event.getBlock().getType())) return;

        Slab blockData = (Slab) event.getBlock().getBlockData();
        if (blockData.getType() != Slab.Type.DOUBLE) return;

        if (isTop(event.getPlayer(), event.getBlock())) {
            blockData.setType(Slab.Type.BOTTOM);
        } else {
            blockData.setType(Slab.Type.TOP);
        }

        event.getBlock().setBlockData(blockData, true);
        event.setCancelled(true);
    }

    private boolean isTop(Player player, Block block) {
        Location start = player.getEyeLocation().clone();
        while (!start.getBlock().equals(block) && start.distance(player.getEyeLocation()) < 6.0D) {
            start.add(player.getEyeLocation().getDirection().multiply(0.05D));
        }
        return start.getY() % 1.0D > 0.5D;
    }
}
