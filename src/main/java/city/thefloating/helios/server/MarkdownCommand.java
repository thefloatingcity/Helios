package city.thefloating.helios.server;

import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.soul.Charon;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

public final class MarkdownCommand {

  private final LangConfig langConfig;
  private final Charon charon;

  @Inject
  public MarkdownCommand(
      final LangConfig langConfig,
      final Charon charon
  ) {
    this.langConfig = langConfig;
    this.charon = charon;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("markdown")
        .meta(CommandMeta.DESCRIPTION, "Toggle markdown chat formatting.")
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.charon.grab(sender).toggleMarkdown()) {
            sender.sendMessage(this.langConfig.c(NodePath.path("markdown", "enabled")));
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("markdown", "disabled")));
          }
        });

    commandManager.command(main);
  }

}
