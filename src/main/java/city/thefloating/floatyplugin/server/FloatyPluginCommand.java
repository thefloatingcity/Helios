package city.thefloating.floatyplugin.server;

import city.thefloating.floatyplugin.FloatyPlugin;
import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.LangConfig;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.spongepowered.configurate.NodePath;

public final class FloatyPluginCommand {

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
