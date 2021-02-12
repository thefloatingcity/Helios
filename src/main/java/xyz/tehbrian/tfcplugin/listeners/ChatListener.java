package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.LuckPermsUtils;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

import java.util.Objects;
import java.util.Set;

public class ChatListener implements Listener {

    private final TFCPlugin main;

    public ChatListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // 1. Format the chat.
        event.setFormat(MiscUtils.color(Objects.requireNonNull(main.getConfig().getString("msg.chat_format"))
                .replace("{prefix}", LuckPermsUtils.getPlayerPrefix(player))
                .replace("{suffix}", LuckPermsUtils.getPlayerSuffix(player))));

        // 2. Color the message.
        if (player.hasPermission("tfcplugin.chatcolor")) {
            event.setMessage(MiscUtils.color(event.getMessage()));
        }

        // 3. Ping other players.
        for (Player pingedPlayer : Bukkit.getOnlinePlayers()) {
            if (pingedPlayer.getUniqueId().equals(player.getUniqueId())) continue;

            String pingedPlayerName = pingedPlayer.getName();
            String pingedPlayerDisplayName = pingedPlayer.getDisplayName();



            final String replacement = ChatColor.GOLD + "$1";

            // 3a. Check player name.
            if (event.getMessage().toLowerCase().contains(pingedPlayerName.toLowerCase())) {
                event.setMessage(replaceCaseSensitive(event.getMessage(), pingedPlayerName, replacement));
                pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
            }

            // 3b. Check player display name.
            if (event.getMessage().toLowerCase().contains(pingedPlayerDisplayName.toLowerCase())) {
                event.setMessage(replaceCaseSensitive(event.getMessage(), pingedPlayerDisplayName, replacement));
                pingedPlayer.playSound(pingedPlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
            }
        }

        // 4. Chat replacements.
        ConfigurationSection replacements = main.getConfig().getConfigurationSection("replacements");
        Set<String> replacementKeys = replacements.getKeys(false);
        for (String from : replacementKeys) {
            event.setMessage(replaceCaseSensitive(event.getMessage(), from, replacements.getString(from)));
        }
    }

    private String replaceCaseSensitive(String message, String from, String to) {
        String lastColors = ChatColor.getLastColors(message);
        return message.replaceAll("(?i)(" + MiscUtils.color(from) + ")", MiscUtils.color(to) + (lastColors.isEmpty() ? ChatColor.RESET : lastColors));
    }

    private String replaceCaseInsensitive(String message, String from, String to) {
        String lastColors = ChatColor.getLastColors(message);
        return message.replaceAll("(" + MiscUtils.color(from) + ")", MiscUtils.color(to) + (lastColors.isEmpty() ? ChatColor.RESET : lastColors));
    }
}
