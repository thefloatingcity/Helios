package city.thefloating.helios.server;

import city.thefloating.helios.Helios;
import city.thefloating.helios.Permission;
import city.thefloating.helios.config.BookDeserializer;
import city.thefloating.helios.config.BooksConfig;
import city.thefloating.helios.config.ConfigConfig;
import city.thefloating.helios.config.LangConfig;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;

public final class RulesCommand {

  private final Helios helios;
  private final BooksConfig booksConfig;
  private final LangConfig langConfig;
  private final ConfigConfig configConfig;

  @Inject
  public RulesCommand(
      final Helios helios,
      final BooksConfig booksConfig,
      final LangConfig langConfig,
      final ConfigConfig configConfig
  ) {
    this.helios = helios;
    this.booksConfig = booksConfig;
    this.langConfig = langConfig;
    this.configConfig = configConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("rules")
        .meta(CommandMeta.DESCRIPTION, "The rules for the server.");

    final var page = main
        .argument(IntegerArgument.<CommandSender>builder("page")
            .withMin(1)
            .withMax(BookDeserializer.pageCount(this.bookNode())) // FIXME: won't work with plugin reload.
            .asOptionalWithDefault(1)
            .build())
        .handler(c -> c.getSender().sendMessage(
            BookDeserializer.deserializePage(this.bookNode(), c.<Integer>get("page"))
        ));

    final var accept = main.literal("accept", ArgumentDescription.of("Whew, that was a lot of reading."))
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (sender.hasPermission(Permission.REALM_MADLANDS)) {
            sender.sendMessage(this.langConfig.c(NodePath.path("rules", "already-accepted")));
          } else {
            // yes, we're going to send a command as the console to promote the
            // player instead of programmatically doing it with the LuckPerms API.
            // if you feel extreme grievance about this, feel free to hire me to
            // remedy this grave issue. my rate is $150/hr.
            if (this.configConfig.data().madlandsEnabled()) {
              // /lp user <player> parent settrack player boarding
              this.setPlayerParent(sender, "mad");
            } else {
              // /lp user <player> parent settrack player passenger
              this.setPlayerParent(sender, "boarding");
            }
            sender.sendMessage(this.langConfig.c(NodePath.path("rules", "accept")));
          }
        });

    commandManager.command(main);
    commandManager.command(page);
    commandManager.command(accept);
  }

  private CommentedConfigurationNode bookNode() {
    return this.booksConfig.rootNode().node("rules");
  }

  private void setPlayerParent(final Player player, final String parentName) {
    this.executeCommand("lp user %s parent settrack player %s".formatted(player.getName(), parentName));
  }

  private void executeCommand(final String command) {
    final Server server = this.helios.getServer();
    server.dispatchCommand(server.getConsoleSender(), command);
  }

}
