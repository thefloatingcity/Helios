package city.thefloating.floatyplugin.realm;

import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.LangConfig;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;

public final class TransposeCommands extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final Transposer transposer;

  @Inject
  public TransposeCommands(
      final LangConfig langConfig,
      final Transposer transposer
  ) {
    this.langConfig = langConfig;
    this.transposer = transposer;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var madlands = commandManager.commandBuilder("madlands")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the madlands.")
        .permission(Permission.WORLD_MADLANDS)
        .senderType(Player.class)
        .handler(c -> this.tryTranspose((Player) c.getSender(), Realm.MADLANDS));

    final var overworld = commandManager.commandBuilder("overworld")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the overworld.")
        .permission(Permission.WORLD_OVERWORLD)
        .senderType(Player.class)
        .handler(c -> this.tryTranspose((Player) c.getSender(), Realm.OVERWORLD));

    final var nether = commandManager.commandBuilder("nether")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the nether.")
        .permission(Permission.WORLD_NETHER)
        .senderType(Player.class)
        .handler(c -> this.tryTranspose((Player) c.getSender(), Realm.NETHER));

    final var end = commandManager.commandBuilder("end")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the end.")
        .permission(Permission.WORLD_END)
        .senderType(Player.class)
        .handler(c -> this.tryTranspose((Player) c.getSender(), Realm.END));

    final var backrooms = commandManager.commandBuilder("backrooms")
        .meta(CommandMeta.DESCRIPTION, "Transpose to the backrooms.")
        .permission(Permission.WORLD_BACKROOMS)
        .senderType(Player.class)
        .handler(c -> this.tryTranspose((Player) c.getSender(), Realm.BACKROOMS));

    commandManager.command(overworld);
    commandManager.command(nether);
    commandManager.command(end);
    commandManager.command(madlands);
    commandManager.command(backrooms);
  }

  private void tryTranspose(final Player player, final Realm destination) {
    final Realm current = Realm.from(player.getWorld());
    if (current == destination) {
      player.sendMessage(this.langConfig.c(NodePath.path("transpose", "already-there")));
      return;
    }

    this.transposer.transpose(player, destination);
  }

}
