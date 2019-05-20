package com.outlook.tehbrian.tfcplugin.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ItemBuilder {

    private Material material;
    private Integer amount = null;
    private Short durability = null;
    private Map<Enchantment, Integer> enchantments = null;
    private Set<ItemFlag> flags = null;
    private String name = null;
    private List<String> lore = null;
    private Boolean unbreakable = null;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public static ItemBuilder importFrom(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return new ItemBuilder(itemStack.getType())
                .amount(itemStack.getAmount())
                .durability(itemStack.getDurability())
                .enchantments(itemStack.getEnchantments())
                .flags(itemStack.getItemFlags())
                .name(itemMeta.getDisplayName())
                .lore(itemMeta.getLore())
                .unbreakable(itemMeta.isUnbreakable());
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

    public ItemBuilder flags(Set<ItemFlag> flags) {
        this.flags = flags;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder unbreakable(Boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {

        ItemStack itemStack = new ItemStack(material);

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

        ItemMeta itemMeta = itemStack.getItemMeta();

        if (name != null) {
            itemMeta.setDisplayName(MiscUtils.color(name));
        }
        if (lore != null) {
            lore.replaceAll(MiscUtils::color);
            itemMeta.setLore(lore);
        }
        if (unbreakable != null) {
            itemMeta.setUnbreakable(unbreakable);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
