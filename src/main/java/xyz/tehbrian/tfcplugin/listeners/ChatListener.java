package xyz.tehbrian.tfcplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.tfcplugin.LuckPermsService;
import xyz.tehbrian.tfcplugin.config.LangConfig;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ChatListener implements Listener {

    private final LangConfig langConfig;
    private final LuckPermsService luckPermsService;

    public ChatListener(
            final @NonNull LangConfig langConfig,
            final @NonNull LuckPermsService luckPermsService
    ) {
        this.langConfig = langConfig;
        this.luckPermsService = luckPermsService;
    }

    @EventHandler
    public void onChat(final AsyncChatEvent event) {
        final Player player = event.getPlayer();

//        // 1. Format the chat.
//        event.setFormat(this.langConfig.c(NodePath.path("chat_format")));
//
//        // 2. Color the message.
//        if (player.hasPermission("tfcplugin.chatcolor")) {
//            event.setMessage(MiscUtils.color(event.getMessage()));
//        }
//
//        // 3. Ping other players.
//        for (final Player pingedPlayer : Bukkit.getOnlinePlayers()) {
//            if (pingedPlayer.getUniqueId().equals(player.getUniqueId())) {
//                continue;
//            }
//
//            final String pingedPlayerName = pingedPlayer.getName();
//            final String pingedPlayerDisplayName = pingedPlayer.getDisplayName();
//
//
//            final String replacement = ChatColor.GOLD + "$1";
//
//            // 3a. Check player name.
//            if (event.getMessage().toLowerCase().contains(pingedPlayerName.toLowerCase())) {
//                event.setMessage(this.replaceCaseSensitive(event.getMessage(), pingedPlayerName, replacement));
//                pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
//            }
//
//            // 3b. Check player display name.
//            if (event.getMessage().toLowerCase().contains(pingedPlayerDisplayName.toLowerCase())) {
//                event.setMessage(this.replaceCaseSensitive(event.getMessage(), pingedPlayerDisplayName, replacement));
//                pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
//            }
//        }
//
//        // 4. Chat replacements.
//        final ConfigurationSection replacements = this.main.getConfig().getConfigurationSection("replacements");
//        final Set<String> replacementKeys = replacements.getKeys(false);
//        for (final String from : replacementKeys) {
//            event.setMessage(this.replaceCaseSensitive(event.getMessage(), from, replacements.getString(from)));
//        }
    }

    private String replaceCaseSensitive(final String message, final String from, final String to) {
        final String lastColors = ChatColor.getLastColors(message);
        return message.replaceAll(
                "(?i)(" + MiscUtils.color(from) + ")",
                MiscUtils.color(to) + (lastColors.isEmpty() ? ChatColor.RESET : lastColors)
        );
    }

    private String replaceCaseInsensitive(final String message, final String from, final String to) {
        final String lastColors = ChatColor.getLastColors(message);
        return message.replaceAll(
                "(" + MiscUtils.color(from) + ")",
                MiscUtils.color(to) + (lastColors.isEmpty() ? ChatColor.RESET : lastColors)
        );
    }

}
