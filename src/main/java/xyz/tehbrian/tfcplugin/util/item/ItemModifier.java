package xyz.tehbrian.tfcplugin.util.item;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Only use if you want to modify existing items.
 * If you would like to create new items, see {@link xyz.tehbrian.tfcplugin.util.item.ItemBuilder}.
 */
public class ItemModifier {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemModifier(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemModifier setMaterial(Material material) {
        itemStack.setType(material);
        return this;
    }

    public ItemModifier setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemModifier addEnchantment(Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemModifier addEnchantments(Map<Enchantment, Integer> enchantments) {
        itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemModifier removeEnchantment(Enchantment enchantment) {
        itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemModifier addFlags(ItemFlag... flags) {
        itemStack.addItemFlags(flags);
        return this;
    }

    public ItemModifier removeFlags(ItemFlag... flags) {
        itemStack.removeItemFlags(flags);
        return this;
    }

    public ItemModifier setName(String name) {
        itemMeta.setDisplayName(MiscUtils.color(name));
        return this;
    }

    public ItemModifier setLore(List<String> lore) {
        itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier addLore(String string) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(MiscUtils.color(string));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier changeLore(int index, String string) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.set(index, MiscUtils.color(string));
        itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier removeLore(int index) {
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        try {
            lore.remove(index);
        } catch (IndexOutOfBoundsException e) {
            Bukkit.broadcastMessage("whoop dee doo");
        }
        itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier setUnbreakable(boolean unbreakable) {
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStack getItem() {
        itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }
}
