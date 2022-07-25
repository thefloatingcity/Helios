package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class DiscordCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;

  @Inject
  public DiscordCommand(final @NonNull LangConfig langConfig) {
    this.langConfig = langConfig;
  }

  /**
   * Register the command.
   *
   * @param commandManager the command manager
   */
  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("discord")
        .meta(CommandMeta.DESCRIPTION, "Links you to our Discord.")
        .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("discord"))));

    commandManager.command(main);
  }

}
