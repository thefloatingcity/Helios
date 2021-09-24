package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Constants;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.config.BooksConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.piano.Instrument;
import xyz.tehbrian.floatyplugin.piano.PianoMenuProvider;
import xyz.tehbrian.floatyplugin.user.UserService;
import xyz.tehbrian.floatyplugin.util.ConfigDeserializers;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PianoCommand extends PaperCloudCommand<CommandSender> {

    private final FloatyPlugin floatyPlugin;
    private final UserService userService;
    private final PianoMenuProvider pianoMenuProvider;
    private final BooksConfig booksConfig;
    private final LangConfig langConfig;

    @Inject
    public PianoCommand(
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull UserService userService,
            final @NonNull PianoMenuProvider pianoMenuProvider,
            final @NonNull BooksConfig booksConfig,
            final @NonNull LangConfig langConfig
    ) {
        this.floatyPlugin = floatyPlugin;
        this.userService = userService;
        this.pianoMenuProvider = pianoMenuProvider;
        this.booksConfig = booksConfig;
        this.langConfig = langConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
        final var main = commandManager.commandBuilder("piano")
                .meta(CommandMeta.DESCRIPTION, "A fancy playable piano.")
                .permission(Constants.Permissions.PIANO)
                .handler(c -> {
                    for (final Component line : ConfigDeserializers.deserializePage(
                            Objects.requireNonNull(this.booksConfig.rootNode())
                                    .node("piano_manual"), 1)) {
                        c.getSender().sendMessage(line);
                    }
                });

        final var help = main.literal("help")
                .argument(IntegerArgument.<CommandSender>newBuilder("page")
                        .withMin(1)
                        .withMax(5)
                        .asOptionalWithDefault(1)
                        .build())
                .handler(c -> {
                    for (final Component line : ConfigDeserializers.deserializePage(
                            Objects.requireNonNull(this.booksConfig.rootNode())
                                    .node("piano_manual"), c.<Integer>get("page"))) {
                        c.getSender().sendMessage(line);
                    }
                });

        final var toggle = main.literal("toggle", ArgumentDescription.of("Toggle your piano on and off."))
                .senderType(Player.class)
                .handler(c -> {
                    final Player sender = (Player) c.getSender();
                    if (this.userService.getUser(sender).piano().toggleEnabled()) {
                        sender.sendMessage(this.langConfig.c(NodePath.path("piano", "enabled")));
                    } else {
                        sender.sendMessage(this.langConfig.c(NodePath.path("piano", "disabled")));
                    }
                });

        final var collection = main.literal("collection", ArgumentDescription.of("Get a collection of notes!"))
                .senderType(Player.class)
                .argument(EnumArgument.of(PianoMenuProvider.NoteCollection.class, "note_collection"))
                .argument(IntegerArgument.<CommandSender>newBuilder("max").withMin(1).asOptionalWithDefault(9).build())
                .handler(c -> {
                    final PianoMenuProvider.NoteCollection noteCollection = c.get("note_collection");
                    final int max = c.get("max");
                    final Player sender = (Player) c.getSender();

                    var delay = 0;
                    final List<ItemStack> noteItems = this.pianoMenuProvider.getCollection(noteCollection);
                    for (final ItemStack item : noteItems.subList(0, Math.min(max, noteItems.size()))) {
                        sender.getServer().getScheduler().scheduleSyncDelayedTask(
                                this.floatyPlugin,
                                () -> {
                                    final var unaddedItem = sender.getInventory().addItem(item).get(0);
                                    if (unaddedItem != null) {
                                        sender.getWorld().dropItem(sender.getLocation(), unaddedItem);
                                        sender.playSound(sender.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7F, 0.7F);
                                    } else {
                                        sender.playSound(sender.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.7F, 1);
                                    }
                                },
                                delay
                        );
                        delay = delay + 1;
                    }
                });

        final var instrument = main.literal("instrument", ArgumentDescription.of("Pick your instrument!"))
                .senderType(Player.class)
                .argument(EnumArgument.of(Instrument.class, "instrument"))
                .handler(c -> {
                    final Instrument inst = c.get("instrument");
                    final Player sender = (Player) c.getSender();

                    this.userService.getUser(sender).piano().instrument(inst);
                    sender.sendMessage(this.langConfig.c(
                            NodePath.path("piano", "instrument_change"),
                            Map.of("instrument", inst.toString())
                    ));
                });

        final var menu = main.literal("menu", ArgumentDescription.of("Pick your notes!"))
                .senderType(Player.class)
                .handler(c -> ((Player) c.getSender()).openInventory(this.pianoMenuProvider.generate()));

        commandManager.command(main);
        commandManager.command(help);
        commandManager.command(toggle);
        commandManager.command(collection);
        commandManager.command(instrument);
        commandManager.command(menu);
    }

}
