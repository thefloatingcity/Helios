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

    public ItemModifier(final ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
    }

    public ItemModifier setMaterial(final Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemModifier setAmount(final int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemModifier addEnchantment(final Enchantment enchantment, final int level) {
        this.itemStack.addEnchantment(enchantment, level);
        return this;
    }

    public ItemModifier addEnchantments(final Map<Enchantment, Integer> enchantments) {
        this.itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemModifier removeEnchantment(final Enchantment enchantment) {
        this.itemStack.removeEnchantment(enchantment);
        return this;
    }

    public ItemModifier addFlags(final ItemFlag... flags) {
        this.itemStack.addItemFlags(flags);
        return this;
    }

    public ItemModifier removeFlags(final ItemFlag... flags) {
        this.itemStack.removeItemFlags(flags);
        return this;
    }

    public ItemModifier setName(final String name) {
        this.itemMeta.setDisplayName(MiscUtils.color(name));
        return this;
    }

    public ItemModifier setLore(final List<String> lore) {
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier addLore(final String string) {
        List<String> lore = this.itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(MiscUtils.color(string));
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier changeLore(final int index, final String string) {
        List<String> lore = this.itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        lore.set(index, MiscUtils.color(string));
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier removeLore(final int index) {
        List<String> lore = this.itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        try {
            lore.remove(index);
        } catch (final IndexOutOfBoundsException e) {
            Bukkit.broadcastMessage("whoop dee doo");
        }
        this.itemMeta.setLore(lore);
        return this;
    }

    public ItemModifier setUnbreakable(final boolean unbreakable) {
        this.itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemStack getItem() {
        this.itemStack.setItemMeta(this.itemMeta);
        return this.itemStack;
    }

}
