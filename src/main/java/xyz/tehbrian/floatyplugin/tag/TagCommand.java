package xyz.tehbrian.floatyplugin.tag;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public final class TagCommand extends PaperCloudCommand<CommandSender> {

  private final LangConfig langConfig;
  private final TagGame tagGame;

  @Inject
  public TagCommand(
      final LangConfig langConfig,
      final TagGame tagGame
  ) {
    this.langConfig = langConfig;
    this.tagGame = tagGame;
  }

  @Override
  public void register(final PaperCommandManager<CommandSender> commandManager) {
    final var main = commandManager.commandBuilder("tag")
        .meta(CommandMeta.DESCRIPTION, "Joins/leaves the game of tag.")
        .senderType(Player.class)
        .handler(c -> {
          final Player sender = (Player) c.getSender();
          if (this.tagGame.togglePlaying(sender)) {
            if (this.tagGame.players().size() <= 1) {
              sender.sendMessage(this.langConfig.c(NodePath.path("tag", "join_first")));
              this.tagGame.it(sender);
            } else {
              sender.sendMessage(this.langConfig.c(NodePath.path("tag", "join")));
            }
          } else {
            sender.sendMessage(this.langConfig.c(NodePath.path("tag", "leave")));
          }
        });

    final var ntb = main.literal("ntb", ArgumentDescription.of("Toggles no tag backs."))
        .handler(c -> {
          if (this.tagGame.toggleNoTagBacks()) {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("tag", "no_tag_backs_enabled")));
          } else {
            c.getSender().sendMessage(this.langConfig.c(NodePath.path("tag", "no_tag_backs_disabled")));
          }
        });

    final var glow = main.literal("glow", ArgumentDescription.of("Sets the glow setting."))
        .argument(EnumArgument.of(GlowSetting.class, "glow_setting"))
        .handler(c -> {
          final GlowSetting glowSetting = c.get("glow_setting");
          this.tagGame.glowSetting(glowSetting);

          c.getSender().sendMessage(this.langConfig.c(
              NodePath.path("tag", "glow"),
              Placeholder.unparsed("glow_setting", glowSetting.name())
          ));
        });

    commandManager.command(main);
    commandManager.command(ntb);
    commandManager.command(glow);
  }

}
