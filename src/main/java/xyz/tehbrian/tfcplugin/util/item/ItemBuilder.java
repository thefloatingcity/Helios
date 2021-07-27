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

    private final Material material;
    private Integer amount = null;
    private Map<Enchantment, Integer> enchantments = null;
    private ItemFlag[] flags = null;
    private String name = null;
    private List<String> lore = null;
    private Boolean unbreakable = null;

    public ItemBuilder(final Material material) {
        this.material = material;
    }

    public ItemBuilder amount(final Integer amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder enchantments(final Map<Enchantment, Integer> enchantments) {
        this.enchantments = enchantments;
        return this;
    }

    public ItemBuilder flags(final ItemFlag... flags) {
        this.flags = flags;
        return this;
    }

    public ItemBuilder name(final String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder unbreakable(final Boolean unbreakable) {
        this.unbreakable = unbreakable;
        return this;
    }

    public ItemStack build() {
        final ItemStack itemStack = new ItemStack(this.material);

        if (this.amount != null) {
            itemStack.setAmount(this.amount);
        }
        if (this.enchantments != null) {
            itemStack.addUnsafeEnchantments(this.enchantments);
        }
        if (this.flags != null) {
            itemStack.addItemFlags(this.flags);
        }

        final ItemMeta itemMeta = itemStack.getItemMeta();

        if (this.name != null) {
            itemMeta.setDisplayName(MiscUtils.color(this.name));
        }
        if (this.lore != null) {
            this.lore.replaceAll(MiscUtils::color);
            itemMeta.setLore(this.lore);
        }
        if (this.unbreakable != null) {
            itemMeta.setUnbreakable(this.unbreakable);
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

}
