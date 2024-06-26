package city.thefloating.helios.server;

import city.thefloating.helios.config.LangConfig;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;

public final class DiscordCommand {

  private final LangConfig langConfig;

  @Inject
  public DiscordCommand(final LangConfig langConfig) {
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("discord")
        .meta(CommandMeta.DESCRIPTION, "Links you to our Discord.")
        .handler(c -> c.getSender().sendMessage(this.langConfig.c(NodePath.path("discord"))));

    commandManager.command(main);
  }

}
