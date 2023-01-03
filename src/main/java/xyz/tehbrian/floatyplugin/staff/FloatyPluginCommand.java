package xyz.tehbrian.floatyplugin.staff;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.Permission;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class FloatyPluginCommand extends PaperCloudCommand<CommandSender> {

  private final FloatyPlugin plugin;
  private final LangConfig langConfig;

  @Inject
  public FloatyPluginCommand(
      final FloatyPlugin plugin,
      final LangConfig langConfig
  ) {
    this.plugin = plugin;
    this.langConfig = langConfig;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("floatyplugin")
        .meta(CommandMeta.DESCRIPTION, "Core commands for FloatyPlugin.");

    final var reload = main.literal("reload", ArgumentDescription.of("Reload the plugin's configs."))
        .permission(Permission.RELOAD)
        .handler(c -> {
          if (this.plugin.loadConfiguration()) {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "successful")));
          } else {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("reload", "unsuccessful")));
          }
        });

    commandManager.command(main);
    commandManager.command(reload);
  }

}
