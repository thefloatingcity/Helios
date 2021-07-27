package xyz.tehbrian.tfcplugin.guis;

import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.item.ItemModifier;

public class MenuGui {

    private MenuGui() {}

    public static Gui generate() {
        Gui gui = new Gui(TFCPlugin.getInstance(), 5, "Server Menu");
        StaticPane pane = new StaticPane(0, 0, 9, 5);

        gui.setOnGlobalClick(event -> event.getWhoClicked().sendMessage("Oh, you clicked on the GUI!"));
        gui.setOnOutsideClick(event -> event.getWhoClicked().sendMessage("Seems like you clicked outside of the GUI!"));

        ItemStack barrier = new ItemModifier(new ItemStack(Material.BARRIER)).setName("&cDO NOT CLICK ON ME").getItem();
        GuiItem barrierGuiItem = new GuiItem(barrier, event -> {
            if (event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                player.kickPlayer("I can't believe it. You really did this to me? And my family?");
            }
        });
        pane.addItem(barrierGuiItem, 0, 0);

        ItemStack ice = new ItemStack(Material.ICE);
        GuiItem guiIceItem = new GuiItem(ice, event -> event.getWhoClicked().sendMessage("You clicked on ice!"));
        pane.addItem(guiIceItem, 2, 2);

        gui.addPane(pane);
        return gui;
    }
}
