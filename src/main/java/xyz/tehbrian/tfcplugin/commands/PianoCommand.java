package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Conditions;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.config.BooksConfig;
import xyz.tehbrian.tfcplugin.piano.Instrument;
import xyz.tehbrian.tfcplugin.piano.PianoMenuProvider;
import xyz.tehbrian.tfcplugin.user.UserService;
import xyz.tehbrian.tfcplugin.util.MsgBuilder;

@SuppressWarnings("unused")
@CommandAlias("piano")
@CommandPermission("tfcplugin.piano")
@Description("A playable piano!")
public class PianoCommand extends BaseCommand {

    private final UserService userService;
    private final PianoMenuProvider pianoMenuProvider;
    private final BooksConfig booksConfig;

    public PianoCommand(
            final @NonNull UserService userService,
            final @NonNull PianoMenuProvider pianoMenuProvider,
            final @NonNull BooksConfig booksConfig
    ) {
        this.userService = userService;
        this.pianoMenuProvider = pianoMenuProvider;
        this.booksConfig = booksConfig;
    }

    @Subcommand("menu")
    @Description("Pick your notes!")
    public void onMenu(final Player player) {
        this.pianoMenuProvider.generate().show(player);
    }

    @Subcommand("instrument")
    @Description("Pick your instrument!")
    @CommandCompletion("*")
    public void onInstrument(final Player player, final Instrument pianoSound) {
        this.userService.getUser(player).piano().instrument(pianoSound);
        player.sendMessage(new MsgBuilder()
                .prefixKey("prefixes.piano.prefix")
                .msgKey("msg.piano.instrument_change")
                .formats(pianoSound.toString())
                .build());
    }

    @Subcommand("toggle")
    @Description("Toggle your piano on and off.")
    public void onToggle(final Player player) {
        if (this.userService.getUser(player).piano().toggleEnabled()) {
            player.sendMessage(new MsgBuilder().prefixKey("prefixes.piano.prefix").msgKey("msg.piano.enabled").build());
        } else {
            player.sendMessage(new MsgBuilder().prefixKey("prefixes.piano.prefix").msgKey("msg.piano.disabled").build());
        }
    }

    @HelpCommand
    public void onHelp(final CommandSender sender, @Default("1") @Conditions("limits:min=1,max=4") final Integer page) {
//        for (final String line : BooksConfig.deserializePage(booksConfig.rootNode().node("piano_manual"), page)) {
//            sender.sendMessage(line);
//        }
    }

}
