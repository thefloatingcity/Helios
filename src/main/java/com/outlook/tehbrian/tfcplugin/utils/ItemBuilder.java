package com.outlook.tehbrian.tfcplugin.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private Material material;
    private Integer amount = null;
    private Short durability = null;
    private Map<Enchantment, Integer> enchantments = null;
    private List<ItemFlag> flags = null;
    private List<String> lore = null;
    private String name = null;
    private Boolean unbreakable = null;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder durability(Short durability) {
        this.durability = durability;
        return this;
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder flags(List<ItemFlag> flags) {
        this.flags = flags;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder unbreakable(Boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {

        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (amount != null) {
            itemStack.setAmount(amount);
        }
        if (durability != null) {
            itemStack.setDurability(durability);
        }
        if (enchantments != null) {
            itemStack.addUnsafeEnchantments(enchantments);
        }
        if (flags != null) {
            for (ItemFlag flag : flags) {
                itemStack.addItemFlags(flag);
            }
        }
        if (lore != null) {
            for (int i = 0; i < lore.size(); i++) {
                lore.set(i, TextUtils.color(lore.get(i)));
            }
            itemStack.setLore(lore);
        }
        if (name != null) {
            itemMeta.setDisplayName(TextUtils.color(name));
        }
        if (unbreakable != null) {
            itemMeta.setUnbreakable(unbreakable);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
