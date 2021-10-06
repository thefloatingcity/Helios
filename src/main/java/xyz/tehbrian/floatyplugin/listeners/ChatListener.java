package xyz.tehbrian.floatyplugin.listeners;

import com.google.inject.Inject;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Constants;
import xyz.tehbrian.floatyplugin.config.EmotesConfig;
import xyz.tehbrian.floatyplugin.config.LangConfig;
import xyz.tehbrian.floatyplugin.util.FormatUtil;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@SuppressWarnings("ClassCanBeRecord")
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
        final Player sender = event.getPlayer();

        @Nullable Player pingedPlayer = null;
        for (final Player onlinePlayer : sender.getServer().getOnlinePlayers()) {
            if (onlinePlayer.getUniqueId().equals(sender.getUniqueId())) {
                continue;
            }

            final String playerName = FormatUtil.plain(onlinePlayer.displayName());

            if (this.containsIgnoreCase(FormatUtil.plain(event.message()), playerName)) {
                pingedPlayer = onlinePlayer;
                onlinePlayer.playSound(onlinePlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
            }
        }

//         * @param source            the message source
//         * @param sourceDisplayName the display name of the source player
//         * @param message           the chat message
//         * @param viewer            the receiving {@link Audience}
//         * @return a rendered chat message

        final @Nullable Player finalPingedPlayer = pingedPlayer;
        event.renderer((source, sourceDisplayName, message, viewer) -> {
            var renderedMessage = message;
            if (sender.hasPermission(Constants.Permissions.CHATCOLOR)) {
                renderedMessage = FormatUtil.legacyWithUrls(message);
            }

            if (finalPingedPlayer != null) {
                renderedMessage = renderedMessage.replaceText(TextReplacementConfig.builder()
                        .match(Pattern.compile(FormatUtil.plain(finalPingedPlayer.displayName()), Pattern.CASE_INSENSITIVE))
                        .replacement((m, b) -> Component.text(m.group(0)).color(NamedTextColor.GOLD))
                        .build());
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
