package xyz.tehbrian.floatyplugin.listener;

import com.google.inject.Inject;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.Permissions;
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
      final EmotesConfig emotesConfig,
      final LangConfig langConfig
  ) {
    this.emotesConfig = emotesConfig;
    this.langConfig = langConfig;
  }

  @EventHandler
  public void onDecorate(final AsyncChatDecorateEvent event) {
    final @Nullable Player source = event.player();
    if (source == null) {
      // no use!
      return;
    }

    Component result = event.originalMessage();

    result = this.color(result, source);
    result = this.decoratePing(result, source, false);
    result = this.replaceEmotes(result);

    event.result(result);
  }

  @EventHandler
  public void onChat(final AsyncChatEvent event) {
    final @Nullable Player source = event.getPlayer();

    Component result = event.originalMessage();

    result = this.color(result, source);
    result = this.decoratePing(result, source, true);
    result = this.replaceEmotes(result);
    result = this.chatFormat(result, source.displayName());

    // I can't be bothered to deal with this right now.
    event.setCancelled(true);
    source.getServer().sendMessage(result);
  }

  private Component color(final Component component, final Player source) {
    if (source.hasPermission(Permissions.CHAT_COLOR)) {
      return FormatUtil.legacyWithUrls(component);
    }
    return component;
  }

  private Component decoratePing(final Component component, final Player source, final boolean playSound) {
    Component result = component;

    for (final Player onlinePlayer : source.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getUniqueId().equals(source.getUniqueId())) {
        continue;
      }

      final String playerName = FormatUtil.plain(onlinePlayer.displayName());

      if (this.containsIgnoreCase(FormatUtil.plain(component), playerName)) {
        if (playSound) { // FIXME: should be separated into another method tbh
          onlinePlayer.playSound(onlinePlayer.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1000, 2);
        }

        result = result.replaceText(TextReplacementConfig.builder()
            .match(Pattern.compile(FormatUtil.plain(onlinePlayer.displayName()), Pattern.CASE_INSENSITIVE))
            .replacement((m, b) -> Component.text(m.group(0)).color(NamedTextColor.GOLD))
            .build());
      }
    }

    return result;
  }

  private Component replaceEmotes(final Component component) {
    Component result = component;

    final CommentedConfigurationNode emotes = this.emotesConfig.rootNode();
    for (final Map.Entry<Object, CommentedConfigurationNode> entry : emotes.childrenMap().entrySet()) {
      result = result.replaceText(TextReplacementConfig.builder()
          .match("(" + entry.getKey() + ")")
          .replacement(FormatUtil.miniMessage(Objects.requireNonNull(entry.getValue().getString())))
          .build());
    }

    return result;
  }

  private Component chatFormat(final Component component, final Component sourceDisplayName) {
    return this.langConfig.c(
        NodePath.path("chat_format"),
        TagResolver.resolver(
            Placeholder.component("sender", sourceDisplayName),
            Placeholder.component("message", component)
        )
    );
  }

  private boolean containsIgnoreCase(final String string, final String that) {
    return string.toLowerCase(Locale.ROOT).contains(that.toLowerCase(Locale.ROOT));
  }

}
