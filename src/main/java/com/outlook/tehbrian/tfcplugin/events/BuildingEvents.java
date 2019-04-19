package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getMaterial() == Material.DRAGON_EGG && !(event.getPlayer().getActiveItem().getType() == Material.DRAGON_EGG)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onIronTrapDoorInteract(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getHand() == EquipmentSlot.HAND) {
                        if (event.getClickedBlock().getType() == Material.IRON_TRAPDOOR) {
                            if (!event.getPlayer().isSneaking()) {
                                Material type = event.getPlayer().getInventory().getItemInHand().getType();
                                if (type.equals(Material.AIR)) {
                                    Bukkit.getScheduler().runTaskLater(main, () -> {
                                        Block block = event.getClickedBlock();
                                        byte da = block.getData();
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
                                        block.setData(data, true);
                                    }, 0L);
                                    event.setCancelled(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onGlazedTerracottaInteract(PlayerInteractEvent event) {
        if (!event.isCancelled()) {
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    if (event.getHand() == EquipmentSlot.HAND) {
                        if (event.getClickedBlock().getType().name().contains("GLAZED")) {
                            if (event.getPlayer().isSneaking()) {
                                Material type = event.getPlayer().getInventory().getItemInHand().getType();
                                if (type.equals(Material.AIR)) {
                                    Bukkit.getScheduler().runTaskLater(main, () -> {
                                        Block block = event.getClickedBlock();
                                        byte da = block.getData();
                                        byte data = (byte) (da + 1);
                                        if (da < 0 || da >= 4) {
                                            data = 0;
                                        }
                                        block.setData(data, true);
                                    }, 0L);
                                    event.setCancelled(true);
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
                    Material type = event.getPlayer().getInventory().getItemInHand().getType();
                    if (!type.equals(Material.STEP) && !type.equals(Material.WOOD_STEP) && !type.equals(Material.STONE_SLAB2) && !type.equals(Material.PURPUR_SLAB)) {
                        return;
                    }
                    byte data;

                    if (event.getClickedBlock().getType() == Material.DOUBLE_STEP && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (Utils.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.STEP);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.STEP);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType() == Material.WOOD_DOUBLE_STEP && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (Utils.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.WOOD_STEP);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.WOOD_STEP);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType() == Material.DOUBLE_STONE_SLAB2 && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (Utils.isTop(event.getPlayer(), event.getClickedBlock())) {
                            event.getClickedBlock().setType(Material.STONE_SLAB2);
                            event.getClickedBlock().setData(data);
                        } else {
                            event.getClickedBlock().setType(Material.STONE_SLAB2);
                            event.getClickedBlock().setData((byte) (data + 8));
                        }
                    }

                    if (event.getClickedBlock().getType() == Material.PURPUR_DOUBLE_SLAB && event.getClickedBlock().getData() <= 7) {
                        event.setCancelled(true);
                        data = event.getClickedBlock().getData();
                        if (Utils.isTop(event.getPlayer(), event.getClickedBlock())) {
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
}
