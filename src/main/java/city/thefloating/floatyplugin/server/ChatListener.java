package city.thefloating.floatyplugin.server;

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
import city.thefloating.floatyplugin.Format;
import city.thefloating.floatyplugin.Permission;
import city.thefloating.floatyplugin.config.EmotesConfig;
import city.thefloating.floatyplugin.config.LangConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ChatListener implements Listener {

  private static final net.kyori.adventure.sound.Sound PING_SOUND = net.kyori.adventure.sound.Sound.sound(
      Sound.BLOCK_NOTE_BLOCK_PLING.key(),
      net.kyori.adventure.sound.Sound.Source.MASTER,
      1000,
      2
  );

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

    event.result(this.colorCodes(event.result(), source));
    event.result(this.colorPingedPlayers(event.result(), source));
    event.result(this.replaceEmotes(event.result()));
  }

  @EventHandler
  public void onChat(final AsyncChatEvent event) {
    final Player source = event.getPlayer();

    this.annoyPingedPlayers(event.message(), source);
    event.message(this.chatFormat(event.message(), source.displayName()));

    event.setCancelled(true); // I can't be bothered to deal with that.
    source.getServer().sendMessage(event.message());
  }

  private Component colorCodes(final Component component, final Player source) {
    if (source.hasPermission(Permission.CHAT_COLOR)) {
      return Format.legacyWithUrls(component);
    }
    return component;
  }

  private Component colorPingedPlayers(final Component component, final Player source) {
    Component result = component;

    for (final Player player : this.getPingedPlayers(component, source)) {
      result = result.replaceText(TextReplacementConfig.builder()
          .match(Pattern.compile(Format.plain(player.displayName()), Pattern.CASE_INSENSITIVE))
          .replacement((m, b) -> Component.text(m.group(0)).color(NamedTextColor.GOLD))
          .build());
    }

    return result;
  }

  private void annoyPingedPlayers(final Component component, final Player source) {
    for (final Player player : this.getPingedPlayers(component, source)) {
      player.playSound(PING_SOUND);
    }
  }

  private List<Player> getPingedPlayers(final Component component, final Player source) {
    final List<Player> list = new ArrayList<>();

    for (final Player onlinePlayer : source.getServer().getOnlinePlayers()) {
      if (onlinePlayer.getUniqueId().equals(source.getUniqueId())) {
        continue;
      }

      final String playerName = Format.plain(onlinePlayer.displayName());
      if (this.containsIgnoreCase(Format.plain(component), playerName)) {
        list.add(onlinePlayer);
      }
    }

    return list;
  }

  private boolean containsIgnoreCase(final String string, final String that) {
    return string.toLowerCase(Locale.ROOT).contains(that.toLowerCase(Locale.ROOT));
  }

  private Component replaceEmotes(final Component component) {
    Component result = component;

    final CommentedConfigurationNode emotes = this.emotesConfig.rootNode();
    for (final Map.Entry<Object, CommentedConfigurationNode> entry : emotes.childrenMap().entrySet()) {
      result = result.replaceText(TextReplacementConfig.builder()
          .match("(" + entry.getKey() + ")")
          .replacement(Format.miniMessage(Objects.requireNonNull(entry.getValue().getString())))
          .build());
    }

    return result;
  }

  private Component chatFormat(final Component component, final Component sourceDisplayName) {
    return this.langConfig.c(
        NodePath.path("chat-format"),
        TagResolver.resolver(
            Placeholder.component("sender", sourceDisplayName),
            Placeholder.component("message", component)
        )
    );
  }

}
