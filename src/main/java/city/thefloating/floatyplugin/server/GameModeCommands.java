package city.thefloating.floatyplugin.server;

import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.LangConfig;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

public final class GameModeCommands {

  private final LangConfig langConfig;

  @Inject
  public GameModeCommands(
      final LangConfig langConfig
  ) {
    this.langConfig = langConfig;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("gamemode", "gm")
        .permission(Permission.GAMEMODE)
        .meta(CommandMeta.DESCRIPTION, "Change your game mode.");

    final var survival = main.literal("survival", "s")
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          sender.setGameMode(GameMode.SURVIVAL);
          sender.sendMessage(this.langConfig.c(
              NodePath.path("gamemode", "change"),
              Placeholder.unparsed("gamemode", "Survival")
          ));
        });

    final var creative = main.literal("creative", "c")
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          sender.setGameMode(GameMode.CREATIVE);
          sender.sendMessage(this.langConfig.c(
              NodePath.path("gamemode", "change"),
              Placeholder.unparsed("gamemode", "Creative")
          ));
        });

    final var adventure = main.literal("adventure", "a")
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          sender.setGameMode(GameMode.ADVENTURE);
          sender.sendMessage(this.langConfig.c(
              NodePath.path("gamemode", "change"),
              Placeholder.unparsed("gamemode", "Adventure")
          ));
        });

    commandManager.command(main);
    commandManager.command(survival);
    commandManager.command(creative);
    commandManager.command(adventure);
  }

}
