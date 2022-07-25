package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.bukkit.parsers.PlayerArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Permissions;
import xyz.tehbrian.floatyplugin.config.ConfigConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;

import java.util.Random;

public final class ActCommands extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final ConfigConfig configConfig;

  @Inject
  public ActCommands(
      final @NonNull LangConfig langConfig,
      final @NonNull ConfigConfig configConfig
  ) {
    this.langConfig = langConfig;
    this.configConfig = configConfig;
  }

  /**
   * Register the command.
   *
   * @param commandManager the command manager
   */
  @Override
  public void register(final @NonNull PaperCommandManager<CommandSender> commandManager) {
    final var zap = commandManager.commandBuilder("zap")
        .senderType(Player.class)
        .permission(Permissions.ZAP)
        .meta(CommandMeta.DESCRIPTION, "Kentucky Fried Player")
        .argument(PlayerArgument.optional("player"))
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          final Player target = c.<Player>getOptional("player").orElse((sender));

          target.getWorld().strikeLightning(target.getLocation());

          if (c.<Player>getOptional("player").isPresent()) {
            sender.getServer().sendMessage(this.langConfig.c(
                NodePath.path("act", "zap_other"),
                TagResolver.resolver(
                    Placeholder.component("issuer", sender.displayName()),
                    Placeholder.component("target", target.displayName())
                )
            ));
          } else {
            sender.getServer().sendMessage(this.langConfig.c(
                NodePath.path("act", "zap_self"),
                TagResolver.resolver(
                    Placeholder.component("issuer", sender.displayName())
                )
            ));
          }
        });

    final var poke = commandManager.commandBuilder("poke")
        .senderType(Player.class)
        .permission(Permissions.POKE)
        .meta(CommandMeta.DESCRIPTION, "Useful for annoying others.")
        .argument(PlayerArgument.optional("player"))
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          final Player target = c.<Player>getOptional("player").orElse((sender));

          final ConfigConfig.Data.PokeForce pokeForce = this.configConfig.data().pokeForce();
          final double maxY = pokeForce.maxY();
          final double minY = pokeForce.minY();
          final double maxXZ = pokeForce.maxXZ();
          final double minXZ = pokeForce.minXZ();

          final Random random = new Random();
          final double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
          final double randY = minY + random.nextDouble() * (maxY - minY);
          final double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
          final Vector randomVector = new Vector(randX, randY, randZ);

          target.setVelocity(randomVector);

          if (c.<Player>getOptional("player").isPresent()) {
            sender.getServer().sendMessage(this.langConfig.c(
                NodePath.path("act", "poke_other"),
                TagResolver.resolver(
                    Placeholder.component("issuer", sender.displayName()),
                    Placeholder.component("target", target.displayName())
                )
            ));
          } else {
            sender.getServer().sendMessage(this.langConfig.c(
                NodePath.path("act", "poke_self"),
                TagResolver.resolver(
                    Placeholder.component("issuer", sender.displayName())
                )
            ));
          }
        });

    commandManager.command(zap);
    commandManager.command(poke);
  }

}
