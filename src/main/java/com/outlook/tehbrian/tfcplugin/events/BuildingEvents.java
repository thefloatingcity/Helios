package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
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

@SuppressWarnings({"unused", "deprecation"})
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
    public void onDragonEggInteract(PlayerInteractEvent event) {
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
                if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    Material material = event.getPlayer().getInventory().getItemInHand().getType();
                    if (material != Material.STEP && material != Material.WOOD_STEP && material != Material.STONE_SLAB2 && material != Material.PURPUR_SLAB) {
                        return;
                    }

                    Block block = event.getClickedBlock();
                    byte data;

                    if (block.getType() == Material.DOUBLE_STEP && block.getData() <= 7) {
                        event.setCancelled(true);
                        data = block.getData();
                        if (MiscUtils.isTop(event.getPlayer(), block)) {
                            block.setType(Material.STEP);
                            block.setData(data);
                        } else {
                            block.setType(Material.STEP);
                            block.setData((byte) (data + 8));
                        }
                    }

                    if (block.getType() == Material.WOOD_DOUBLE_STEP && block.getData() <= 7) {
                        event.setCancelled(true);
                        data = block.getData();
                        if (MiscUtils.isTop(event.getPlayer(), block)) {
                            block.setType(Material.WOOD_STEP);
                            block.setData(data);
                        } else {
                            block.setType(Material.WOOD_STEP);
                            block.setData((byte) (data + 8));
                        }
                    }

                    if (block.getType() == Material.DOUBLE_STONE_SLAB2 && block.getData() <= 7) {
                        event.setCancelled(true);
                        data = block.getData();
                        if (MiscUtils.isTop(event.getPlayer(), block)) {
                            block.setType(Material.STONE_SLAB2);
                            block.setData(data);
                        } else {
                            block.setType(Material.STONE_SLAB2);
                            block.setData((byte) (data + 8));
                        }
                    }

                    if (block.getType() == Material.PURPUR_DOUBLE_SLAB && block.getData() <= 7) {
                        event.setCancelled(true);
                        data = block.getData();
                        if (MiscUtils.isTop(event.getPlayer(), block)) {
                            block.setType(Material.PURPUR_SLAB);
                            block.setData(data);
                        } else {
                            block.setType(Material.PURPUR_SLAB);
                            block.setData((byte) (data + 8));
                        }
                    }
                }
            }
        }
    }
}
