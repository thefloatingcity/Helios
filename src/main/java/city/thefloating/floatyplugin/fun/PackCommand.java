package city.thefloating.floatyplugin.fun;

import city.thefloating.floatyplugin.config.ConfigConfig;
import city.thefloating.floatyplugin.config.LangConfig;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

public final class PackCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final ConfigConfig configConfig;

  @Inject
  public PackCommand(
      final LangConfig langConfig,
      final ConfigConfig configConfig
  ) {
    this.langConfig = langConfig;
    this.configConfig = configConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
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
