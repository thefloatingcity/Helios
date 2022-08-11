package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class PackCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final ConfigConfig configConfig;

  @Inject
  public PackCommand(
      final @NonNull LangConfig langConfig,
      final @NonNull ConfigConfig configConfig
  ) {
    this.langConfig = langConfig;
    this.configConfig = configConfig;
  }

  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("pack")
        .senderType(Player.class)
        .meta(CommandMeta.DESCRIPTION, "Get the fancy server resource pack.")
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          sender.setResourcePack(
              this.configConfig.data().resourcePackUrl(),
              this.configConfig.data().resourcePackHash()
          );
          sender.sendMessage(this.langConfig.c(NodePath.path("resource_pack", "sending")));
        });

    commandManager.command(main);
  }

}
