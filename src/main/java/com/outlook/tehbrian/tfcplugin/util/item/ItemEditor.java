package com.outlook.tehbrian.tfcplugin.util.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemEditor {

    private ItemStack itemStack;

    public ItemEditor(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemEditor setMaterial(Material material) {
        return this;
    }

    public ItemEditor setAmount(Integer amount) {
        return this;
    }

    public ItemEditor setDurability(Short durability) {
        return this;
    }

    public ItemEditor setEnchantments(Map<Enchantment, Integer> enchantments) {
        return this;
    }

    public ItemEditor setFlags(Set<ItemFlag> flags) {
        return this;
    }

    public ItemEditor setName(String name) {
        return this;
    }

    public ItemEditor setLore(List<String> lore) {
        return this;
    }

    public ItemEditor setLore(String... lore) {
        return this;
    }

    public ItemEditor editLore(int index, String lore) {
        return this;
    }

    public ItemEditor addLore(String string) {


        return this;
    }

    public ItemEditor removeLore(int index) {
        return this;
    }

    public ItemEditor setUnbreakable(Boolean unbreakable) {
        return this;
    }
}
