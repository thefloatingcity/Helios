package city.thefloating.helios.nextbot;

import city.thefloating.helios.Permission;
import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class NextbotCommand {

  private final Nate nate;

  @Inject
  public NextbotCommand(
      final Nate nate
  ) {
    this.nate = nate;
  }

  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var nextbot = commandManager.commandBuilder("nextbot")
        .meta(CommandMeta.DESCRIPTION, "Manage nextbots.")
        .permission(Permission.NEXTBOT);

    final var killAll = nextbot.literal("kill-all", ArgumentDescription.of("Kill all nextbots globally."))
        .handler(c -> this.nate.killNextbots());

    final var summon = nextbot.literal("summon", ArgumentDescription.of("Summon a nextbot."))
        .senderType(Player.class)
        .argument(EnumArgument.of(Nextbot.Type.class, "type"))
        .handler(c -> {
          final var sender = (Player) c.getSender();
          this.nate.createNextbot(c.get("type"), sender.getLocation());
        });

    commandManager.command(killAll);
    commandManager.command(summon);
  }

}
