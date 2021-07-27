package xyz.tehbrian.tfcplugin.guis;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import xyz.tehbrian.tfcplugin.TFCPlugin;

public class BlocksGui {

    private BlocksGui() {
    }

    public static Gui generate() {
        final Gui gui = new Gui(TFCPlugin.getInstance(), 1, "Super Secret Building Blocks");
        final OutlinePane pane = new OutlinePane(0, 0, 9, 1);

        pane.addItem(new GuiItem(new ItemStack(Material.BARRIER)));
        pane.addItem(new GuiItem(new ItemStack(Material.DRAGON_EGG)));

        gui.addPane(pane);
        return gui;
    }

}
