package xyz.tehbrian.tfcplugin.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.LuckPermsService;
import xyz.tehbrian.tfcplugin.config.LangConfig;
import xyz.tehbrian.tfcplugin.util.MiscUtils;

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
