package city.thefloating.helios.server;

import city.thefloating.helios.config.LangConfig;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;

public final class VoteCommand {

  private final LangConfig langConfig;

  @Inject
  public VoteCommand(final LangConfig langConfig) {
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("vote")
        .meta(CommandMeta.DESCRIPTION, "Show a list of voting sites.")
        .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("vote"))));

    commandManager.command(main);
  }

}
