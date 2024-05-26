package city.thefloating.helios.server;

import city.thefloating.helios.ChatFormat;
import city.thefloating.helios.Permission;
import city.thefloating.helios.config.EmotesConfig;
import city.thefloating.helios.config.LangConfig;
import city.thefloating.helios.soul.Charon;
import com.google.inject.Inject;
import io.papermc.paper.event.player.AsyncChatDecorateEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.NodePath;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

public final class ChatListener implements Listener {

  private final EmotesConfig emotesConfig;
  private final LangConfig langConfig;
  private final Charon charon;

  @Inject
  public ChatListener(
      final EmotesConfig emotesConfig,
      final LangConfig langConfig,
      final Charon charon
  ) {
    this.emotesConfig = emotesConfig;
    this.langConfig = langConfig;
    this.charon = charon;
  }

  @EventHandler
  public void onDecorate(final AsyncChatDecorateEvent event) {
    final @Nullable Player source = event.player();
    if (source == null) {
      // no use!
      return;
    }

    event.result(this.colorCodes(event.result(), source));
    event.result(this.markdown(event.result(), source));
    event.result(this.colorPingedPlayers(event.result(), source));
    event.result(this.replaceEmotes(event.result()));
    event.result(this.greentext(event.result())); // greentext last to overwrite all other colors.
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
      return ChatFormat.legacyWithUrls(component);
    }
    return component;
  }

  private static final TextColor GREENTEXT_COLOR = TextColor.fromHexString("#789922");
  private static final Pattern GREENTEXT_PATTERN = Pattern.compile("^>[a-zA-Z0-9!?.]");

  private Component greentext(final Component component) {
    if (GREENTEXT_PATTERN.matcher(ChatFormat.plain(component)).find()) {
      return component.color(GREENTEXT_COLOR);
    }
    return component;
  }

  private static final Pattern BOLD_PATTERN = Pattern.compile("\\*\\*(.*)\\*\\*");
  private static final Pattern ITALIC_PATTERN = Pattern.compile("\\*(.*?)\\*");
  private static final Pattern UNDERLINED_PATTERN = Pattern.compile("__(.*)__");
  private static final Pattern STRIKETHROUGH_PATTERN = Pattern.compile("~~(.*)~~");

  private Component markdown(final Component component, final Player source) {
    if (!this.charon.grab(source).markdown()) {
      return component;
    }

    return component
        .replaceText(t -> t
            .match(BOLD_PATTERN)
            .replacement((m, b) -> Component.text(m.group(1)).decorate(TextDecoration.BOLD)))
        .replaceText(t -> t
            .match(ITALIC_PATTERN)
            .replacement((m, b) -> Component.text(m.group(1)).decorate(TextDecoration.ITALIC)))
        .replaceText(t -> t
            .match(UNDERLINED_PATTERN)
            .replacement((m, b) -> Component.text(m.group(1)).decorate(TextDecoration.UNDERLINED)))
        .replaceText(t -> t
            .match(STRIKETHROUGH_PATTERN)
            .replacement((m, b) -> Component.text(m.group(1)).decorate(TextDecoration.STRIKETHROUGH)));
  }

  private Component colorPingedPlayers(final Component component, final Player source) {
    Component result = component;

    for (final Player player : this.getPingedPlayers(component, source)) {
      result = result.replaceText(TextReplacementConfig.builder()
          .match(Pattern.compile(ChatFormat.plain(player.displayName()), Pattern.CASE_INSENSITIVE))
          .replacement((m, b) -> Component.text(m.group(0)).color(NamedTextColor.GOLD))
          .build());
    }

    return result;
  }

  private static final net.kyori.adventure.sound.Sound PING_SOUND = net.kyori.adventure.sound.Sound.sound(
      Sound.BLOCK_NOTE_BLOCK_PLING.key(),
      net.kyori.adventure.sound.Sound.Source.MASTER,
      1000,
      2
  );

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

      final String playerName = ChatFormat.plain(onlinePlayer.displayName());
      if (this.containsIgnoreCase(ChatFormat.plain(component), playerName)) {
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
          .replacement(ChatFormat.miniMessage(Objects.requireNonNull(entry.getValue().getString())))
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
