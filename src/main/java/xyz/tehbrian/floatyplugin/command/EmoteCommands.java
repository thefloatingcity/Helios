package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public class EmoteCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;

    @Inject
    public EmoteCommands(
            final @NonNull LangConfig langConfig
    ) {
        this.langConfig = langConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
//@CommandAlias("unreadable")
//    @CommandPermission("floatyplugin.emote.unreadable")
//    @Description("Untransparent. Is that a word? Opaque?")
//    public void onUnreadable(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.unreadable").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("cry")
//    @CommandPermission("floatyplugin.emote.cry")
//    @Description("So sad.")
//    public void onCry(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.cry").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("shrug")
//    @CommandPermission("floatyplugin.emote.shrug")
//    @Description("You don't know. They don't know.")
//    public void onShrug(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.shrug").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("hug")
//    @CommandPermission("floatyplugin.emote.hug")
//    @Description("D'aww that's so cute!")
//    public void onHug(final Player player, final String text) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.hug").formats(player.getDisplayName(), text).build());
//    }
//
//    @CommandAlias("blame")
//    @CommandPermission("floatyplugin.emote.blame")
//    @Description("It's their fault, not yours.")
//    public void onBlame(final Player player, final String text) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.blame").formats(player.getDisplayName(), text).build());
//    }
//
//    @CommandAlias("winkwonk")
//    @CommandPermission("floatyplugin.emote.winkwonk")
//    @Description("Wink wonk ;)")
//    public void onWinkWonk(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.winkwonk").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("sue")
//    @CommandPermission("floatyplugin.emote.sue")
//    @Description("Court fixes everything.. right?")
//    public void onSue(final Player player, @Optional final String text) {
//        if (text == null) {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.sue").formats(player.getDisplayName()).build());
//        } else {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.sue_extra").formats(player.getDisplayName(), text).build());
//        }
//    }
//
//    @CommandAlias("highfive")
//    @CommandPermission("floatyplugin.emote.highfive")
//    @Description("Up high! Down low! Too slow!")
//    public void onHighfive(final Player player, final String text) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.highfive").formats(player.getDisplayName(), text).build());
//    }
//
//    @CommandAlias("spook")
//    @CommandPermission("floatyplugin.emote.spook")
//    @Description("OoooOOooOoOOoOOoo")
//    public void onSpook(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.spook").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("doubt")
//    @CommandPermission("floatyplugin.emote.doubt")
//    @Description("Press X.")
//    public void onDoubt(final Player player) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.doubt").formats(player.getDisplayName()).build());
//    }
//
//    @CommandAlias("kith")
//    @CommandPermission("floatyplugin.emote.kith")
//    @Description("It's kiss, with a lisp.")
//    public void onKith(final Player player, final String text) {
//        Bukkit.broadcastMessage(new MsgBuilder().msgKey("emote.kith").formats(player.getDisplayName(), text).build());
//    }
    }

}
