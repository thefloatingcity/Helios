package city.thefloating.floatyplugin.piano;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.BookDeserializer;
import city.thefloating.floatyplugin.config.BooksConfig;
import city.thefloating.floatyplugin.config.LangConfig;
import city.thefloating.floatyplugin.soul.Charon;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;

import java.util.List;

public final class PianoCommand {

  private final FloatyPlugin plugin;
  private final Charon charon;
  private final PianoMenuProvider pianoMenuProvider;
  private final BooksConfig booksConfig;
  private final LangConfig langConfig;

  @Inject
  public PianoCommand(
      final FloatyPlugin plugin,
      final Charon charon,
      final PianoMenuProvider pianoMenuProvider,
      final BooksConfig booksConfig,
      final LangConfig langConfig
  ) {
    this.plugin = plugin;
    this.charon = charon;
    this.pianoMenuProvider = pianoMenuProvider;
    this.booksConfig = booksConfig;
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("piano")
        .meta(CommandMeta.DESCRIPTION, "A fancy playable piano.")
        .permission(Permission.PIANO)
        .handler(c -> c.getSender().sendMessage(
            BookDeserializer.deserializePage(this.getBookNode(), 1)
        ));

    final var help = main.literal("help")
        .argument(IntegerArgument.<CommandSender>builder("page")
            .withMin(1)
            .withMax(BookDeserializer.pageCount(this.getBookNode()))
            .asOptionalWithDefault(1)
            .build())
        .handler(c -> c.getSender().sendMessage(
            BookDeserializer.deserializePage(this.getBookNode(), c.<Integer>get("page"))
        ));

    final var toggle = main.literal("toggle", ArgumentDescription.of("Toggle your piano on and off."))
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.charon.getSoul(sender).piano().toggleEnabled()) {
            sender.sendMessage(this.langConfig.c(NodePath.path("piano", "enabled")));
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("piano", "disabled")));
          }
        });

    final var collection = main.literal("collection", ArgumentDescription.of("Get a collection of notes!"))
        .senderType(Player.class)
        .argument(EnumArgument.of(PianoMenuProvider.NoteCollection.class, "note_collection"))
        .argument(IntegerArgument.<CommandSender>builder("max").withMin(1).asOptionalWithDefault(9).build())
        .handler(c -> {
          final PianoMenuProvider.NoteCollection noteCollection = c.get("note_collection");
          final int max = c.get("max");
          final Player sender = (Player) c.getSender();

          var delay = 0;
          final List<ItemStack> noteItems = this.pianoMenuProvider.getCollection(noteCollection);
          for (final ItemStack item : noteItems.subList(0, Math.min(max, noteItems.size()))) {
            sender.getServer().getScheduler().scheduleSyncDelayedTask(
                this.plugin,
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

          this.charon.getSoul(sender).piano().instrument(inst);
          sender.sendMessage(this.langConfig.c(
              NodePath.path("piano", "instrument-change"),
              Placeholder.unparsed("instrument", inst.toString())
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

  public CommentedConfigurationNode getBookNode() {
    return this.booksConfig.rootNode().node("piano-manual");
  }

}
