package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class BuildingEvents implements Listener {

    private final Main main;

    public BuildingEvents(Main main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDragonEggTeleport(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getMaterial() == Material.DRAGON_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSoilTrample(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL && event.getMaterial() == Material.SOIL) {
            Block block = event.getClickedBlock();
            event.setUseInteractedBlock(Event.Result.DENY);
            event.setCancelled(true);
            block.setTypeIdAndData(block.getType().getId(), block.getData(), true);
        }
    }

    @EventHandler
    public void onIronTrapDoorInteract(final PlayerInteractEvent e) {
        if (!e.isCancelled()) {
            if (!ironTrapdoorNames.contains(e.getPlayer().getName())) {
                if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        if (e.getClickedBlock().getType().equals(Material.IRON_TRAPDOOR)) {
                            if (!nmsManager.isAtLeastVersion(1, 9, 0) || e.getHand().equals(EquipmentSlot.HAND)) {
                                if (!e.getPlayer().isSneaking()) {
                                    Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                                        public void run() {
                                            Block b = e.getClickedBlock();
                                            byte da = b.getData();
                                            byte data = 0;
                                            if (da >= 0 && da < 4) {
                                                data = (byte) (da + 4);
                                            } else if (da >= 4 && da < 8) {
                                                data = (byte) (da - 4);
                                            } else if (da >= 8 && da < 12) {
                                                data = (byte) (da + 4);
                                            } else if (da >= 12 && da < 16) {
                                                data = (byte) (da - 4);
                                            }

                                            b.setData(data, true);
                                        }
                                    }, 0L);
                                    e.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void GlazedTerracottaInteract(PlayerInteractEvent e) {
        if (!e.isCancelled()) {
            if (nmsManager.isAtLeastVersion(1, 12, 0)) {
                if (terracottaNames.contains(e.getPlayer().getName())) {
                    if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                        if (e.getHand().equals(EquipmentSlot.HAND)) {
                            if (e.getClickedBlock().getType().name().contains("GLAZED")) {
                                if (e.getPlayer().isSneaking()) {
                                    Material type = e.getPlayer().getInventory().getItemInHand().getType();
                                    if (type.equals(Material.AIR)) {
                                        Bukkit.getScheduler().runTaskLater(this, () -> {
                                            Block b = e.getClickedBlock();
                                            byte da = b.getData();
                                            byte data = (byte) (da + 1);
                                            if (da < 0 || da >= 4) {
                                                data = 0;
                                            }

                                            b.setData(data, true);
                                        }, 0L);
                                        e.setCancelled(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    byte data;
                    if (event.getClickedBlock().getType().equals(Material.DOUBLE_STEP) && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (this.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.STEP);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.STEP);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType().equals(Material.WOOD_DOUBLE_STEP) && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (this.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.WOOD_STEP);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.WOOD_STEP);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType().equals(Material.DOUBLE_STONE_SLAB2) && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (this.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.STONE_SLAB2);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.STONE_SLAB2);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType().equals(Material.PURPUR_DOUBLE_SLAB) && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (this.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.PURPUR_SLAB);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.PURPUR_SLAB);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }
                }
            }
        }
    }

    private boolean isTop(Player p, Block b) {
        Location start = p.getEyeLocation().clone();

        while (!start.getBlock().equals(b) && start.distance(p.getEyeLocation()) < 6.0D) {
            start.add(p.getLocation().getDirection().multiply(0.05D));
        }

        return start.getY() % 1.0D > 0.5D;
    }
}
