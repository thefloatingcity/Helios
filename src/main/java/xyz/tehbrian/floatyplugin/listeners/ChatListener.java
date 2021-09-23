package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class ChatListener implements Listener {

    private final EmotesConfig emotesConfig;
    private final LangConfig langConfig;

    @Inject
    public ChatListener(
            final @NonNull EmotesConfig emotesConfig,
            final @NonNull LangConfig langConfig
    ) {
        this.emotesConfig = emotesConfig;
        this.langConfig = langConfig;
    }

//    no-permission: §cI'm sorry, but you don't have permission for that command.
//    connection-throttle: §c»§6»§e»§f Connection throttled. Please wait a moment before reconnecting. §e«§6«§c«
//    authentication-servers-down: §4»§c»§6»§f Mojang's authentication servers seem to be down. Try joining again in a bit! §6«§c«§4«
//    flying-player: §f»§7»§8»§f You can't fly in The Floating City! Perhaps it was a glitch, or maybe lag? §8«§7«§f«
//    flying-vehicle: §f»§7»§8»§f You can't fly in The Floating City! Perhaps it was a glitch, or maybe lag? §8«§7«§f«
//    whitelist: §f»§7»§8»§f You aren't whitelisted on this server! §8«§7«§f«
//    unknown-command: §fUnrecognized command. Try §e/help§f for a list of commands.
//    server-full: §4»§c»§6»§f The Floating City is currently full! Try joining again in a bit! §6«§c«§4«
//    outdated-client: §1»§9»§3»§f The Floating City is running a newer version! Please use {0} §3«§9«§1«
//    outdated-server: §1»§9»§3»§f The Floating City is running an older version! Please use {0} §3«§9«§1«
//    restart: §9»§3»§b»§f The Floating City is restarting. Come back in a moment! §b«§3«§9«
//    shutdown-message: §f»§7»§8»§f The Floating City has been closed. See ya soon! §8«§7«§f«
//    motd=\u00A77                -\=[ \u00A7fThe \u00A7bFloating \u00A7aCity \u00A77]\=-\n      \u00A77Creative building in a void world with no fly.

    @EventHandler
    public void onChat(final AsyncChatEvent event) {
        final Player player = event.getPlayer();

//         * @param source            the message source
//         * @param sourceDisplayName the display name of the source player
//         * @param message           the chat message
//         * @param viewer            the receiving {@link Audience}
//         * @return a rendered chat message

        event.renderer((source, sourceDisplayName, message, viewer) -> {
            var renderedMessage = message;
            if (player.hasPermission("floatyplugin.chatcolor")) {
                renderedMessage = FormatUtil.legacy(message);
            }

            for (final Player onlinePlayer : player.getServer().getOnlinePlayers()) {
                if (onlinePlayer.getUniqueId().equals(player.getUniqueId())) {
                    continue;
                }

                final String playerName = FormatUtil.plain(onlinePlayer.name());

                if (this.containsIgnoreCase(FormatUtil.plain(message), playerName)) {
                    renderedMessage = renderedMessage.replaceText(TextReplacementConfig.builder()
                            .match("(?i)(" + playerName + ")")
                            .replacement(onlinePlayer.name().color(NamedTextColor.GOLD))
                            .build());
                    onlinePlayer.playSound(onlinePlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
                }
            }

            final CommentedConfigurationNode emotes = this.emotesConfig.rootNode();
            for (final Map.Entry<Object, CommentedConfigurationNode> entry : emotes.childrenMap().entrySet()) {
                renderedMessage = renderedMessage.replaceText(TextReplacementConfig.builder()
                        .match("(" + entry.getKey() + ")")
                        .replacement(FormatUtil.miniMessage(Objects.requireNonNull(entry.getValue().getString())))
                        .build());
            }

            return this.langConfig.c(
                    NodePath.path("chat_format"),
                    Template.of("sender", sourceDisplayName),
                    Template.of("message", renderedMessage)
            );
        });
    }

    private boolean containsIgnoreCase(final String string, final String that) {
        return string.toLowerCase(Locale.ROOT).contains(that.toLowerCase(Locale.ROOT));
    }

}
