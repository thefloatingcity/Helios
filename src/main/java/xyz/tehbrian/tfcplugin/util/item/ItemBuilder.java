package xyz.tehbrian.tfcplugin.util.item;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

import java.util.List;
import java.util.Map;

/**
 * Only use if you want to create new items.
 * If you would like to modify existing items, see {@link xyz.tehbrian.tfcplugin.util.item.ItemModifier}.
 */
public class ItemBuilder {

    private Material material;
    private Integer amount = null;
    private Map<Enchantment, Integer> enchantments = null;
    private ItemFlag[] flags = null;
    private String name = null;
    private List<String> lore = null;
    private Boolean unbreakable = null;

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder enchantments(Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder flags(ItemFlag... flags) {
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
        if (enchantments != null) {
            itemStack.addUnsafeEnchantments(enchantments);
        }
        if (flags != null) {
            itemStack.addItemFlags(flags);
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
