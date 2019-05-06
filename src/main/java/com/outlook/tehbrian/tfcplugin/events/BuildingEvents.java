package com.outlook.tehbrian.tfcplugin.events;

import com.outlook.tehbrian.tfcplugin.Main;
import com.outlook.tehbrian.tfcplugin.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.SignChangeEvent;
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
    public void onCropsTrample(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            if (event.getClickedBlock().getType() == Material.SOIL) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDragonEggInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getMaterial() == Material.DRAGON_EGG && !(event.getPlayer().getActiveItem().getType() == Material.DRAGON_EGG)) {
            event.setCancelled(true);
        }
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

    @EventHandler(ignoreCancelled = true)
    public void onSignClick(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    BlockState blockState = event.getClickedBlock().getState();
                    if (blockState instanceof Sign) {
                        if (!event.getPlayer().isSneaking()) {
                            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.SIGN) {
                                Bukkit.getScheduler().runTask(main, () -> {
                                    Sign sign = (Sign) blockState;
                                    event.getPlayer().openSign(sign);
                                });
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onIronTrapDoorInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    if (event.getClickedBlock().getType() == Material.IRON_TRAPDOOR) {
                        if (!event.getPlayer().isSneaking()) {
                            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                                Bukkit.getScheduler().runTask(main, () -> {
                                    Block block = event.getClickedBlock();
                                    byte data = block.getData();
                                    byte newData = 0;
                                    if (data >= 0 && data < 4) {
                                        newData = (byte) (data + 4);
                                    } else if (data >= 4 && data < 8) {
                                        newData = (byte) (data - 4);
                                    } else if (data >= 8 && data < 12) {
                                        newData = (byte) (data + 4);
                                    } else if (data >= 12 && data < 16) {
                                        newData = (byte) (data - 4);
                                    }
                                    block.setData(newData, true);
                                });
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }

    }

    @EventHandler(ignoreCancelled = true)
    public void onGlazedTerracottaInteract(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (event.getHand() == EquipmentSlot.HAND) {
                    if (event.getClickedBlock().getType().name().contains("GLAZED")) {
                        if (event.getPlayer().isSneaking()) {
                            if (event.getPlayer().getInventory().getItemInMainHand().getType() == Material.AIR) {
                                Bukkit.getScheduler().runTask(main, () -> {
                                    Block block = event.getClickedBlock();
                                    byte data = block.getData();
                                    byte newData = (byte) (data + 1);
                                    if (data < 0 || data >= 4) {
                                        data = 0;
                                    }
                                    block.setData(data, true);
                                });
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onSlabBreak(PlayerInteractEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Material material = event.getPlayer().getInventory().getItemInMainHand().getType();
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
