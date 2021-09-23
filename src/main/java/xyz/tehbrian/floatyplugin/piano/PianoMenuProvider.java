package xyz.tehbrian.floatyplugin.piano;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.github.stefvanschie.inventoryframework.Gui;
import com.github.stefvanschie.inventoryframework.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.google.inject.Inject;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurationNode;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.InventoriesConfig;

import java.util.Objects;

public final class PianoMenuProvider {

    private final FloatyPlugin floatyPlugin;
    private final InventoriesConfig inventoriesConfig;

    @Inject
    public PianoMenuProvider(
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull InventoriesConfig inventoriesConfig
    ) {
        this.floatyPlugin = floatyPlugin;
        this.inventoriesConfig = inventoriesConfig;
    }

    public Gui generate() {
        final CommentedConfigurationNode pianoNotesNode = Objects.requireNonNull(this.inventoriesConfig.rootNode()).node("piano_notes");
        final CommentedConfigurationNode itemsNode = Objects.requireNonNull(pianoNotesNode).node("items");

        final Gui gui = new Gui(3, Objects.requireNonNull(pianoNotesNode.node("name").getString()));
        final OutlinePane pane = new OutlinePane(0, 0, 9, 3);

        for (final ConfigurationNode itemNode : Objects.requireNonNull(itemsNode).childrenList()) {
            pane.addItem(new GuiItem(PaperItemBuilder.ofType(
                    Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(itemNode.node("material").getString())))
            )
                    .amount(itemNode.node("amount").getInt(1))
                    .name(MiniMessage.get().parse(Objects.requireNonNull(itemNode.node("name").getString())))
                    .lore(Component.text("uwu broke ur lore again!! nyaa~"))
                    .unbreakable(itemNode.node("unbreakable").getBoolean(false))
                    .build()));
        }

        gui.addPane(pane);
        return gui;
    }

}
