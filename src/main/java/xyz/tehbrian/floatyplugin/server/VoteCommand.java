package xyz.tehbrian.floatyplugin.server;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class VoteCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;

  @Inject
  public VoteCommand(final LangConfig langConfig) {
    this.langConfig = langConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("vote")
        .meta(CommandMeta.DESCRIPTION, "Show a list of voting sites.")
        .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("vote"))));

    commandManager.command(main);
  }

}
