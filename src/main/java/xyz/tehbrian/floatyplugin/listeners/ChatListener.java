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

public final class ChatListener implements Listener {

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
