package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class FloatyPluginCommand extends PaperCloudCommand<CommandSender> {

  private final FloatyPlugin floatyPlugin;
  private final LangConfig langConfig;

  @Inject
  public FloatyPluginCommand(
      final FloatyPlugin floatyPlugin,
      final LangConfig langConfig
  ) {
    this.floatyPlugin = floatyPlugin;
    this.langConfig = langConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("floatyplugin")
        .meta(CommandMeta.DESCRIPTION, "Core commands for FloatyPlugin.");

    final var reload = main.literal("reload", ArgumentDescription.of("Reload the plugin's configs."))
        .permission(Permissions.RELOAD)
        .handler(c -> {
          if (this.floatyPlugin.loadConfiguration()) {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "successful")));
          } else {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "unsuccessful")));
          }
        });

    commandManager.command(main);
    commandManager.command(reload);
  }

}
