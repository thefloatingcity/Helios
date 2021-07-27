package xyz.tehbrian.tfcplugin.guis;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.item.ItemBuilder;

import java.util.Objects;

public class PianoMenuGui {

    private PianoMenuGui() {
    }

    public static Gui generate() {
        final ConfigurationSection invConfigSection = TFCPlugin.getInstance().getConfig().getConfigurationSection("inventories.piano_notes");
        final ConfigurationSection items = Objects.requireNonNull(invConfigSection).getConfigurationSection("items");

        final Gui gui = new Gui(TFCPlugin.getInstance(), 3, Objects.requireNonNull(invConfigSection.getString("name")));
        final OutlinePane pane = new OutlinePane(0, 0, 9, 3);

        for (final String key : Objects.requireNonNull(items).getKeys(false)) {
            final ConfigurationSection item = items.getConfigurationSection(key);

            pane.addItem(new GuiItem(new ItemBuilder(Material.matchMaterial(Objects.requireNonNull(Objects
                    .requireNonNull(item)
                    .getString("material"))))
                    .amount(item.isSet("amount") ? item.getInt("amount") : 1)
                    .name(item.getString("name"))
                    .lore(item.getStringList("lore"))
                    .unbreakable(item.getBoolean("unbreakable"))
                    .build()));
        }

        gui.addPane(pane);
        return gui;
    }

}
