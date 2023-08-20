package city.thefloating.floatyplugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public final class Format {

  public static final LegacyComponentSerializer LEGACY_WITH_URLS = LegacyComponentSerializer
      .builder()
      .character('&')
      .extractUrls()
      .build();
  public static final LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
  public static final PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
  public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  private Format() {
  }

  public static Component legacyWithUrls(final Component component) {
    return LEGACY_WITH_URLS.deserialize(plain(component));
  }

  public static Component reverseLegacy(final Component component) {
    return plain(LEGACY.serialize(component));
  }

  public static Component legacy(final Component component) {
    return legacy(plain(component));
  }

  public static Component legacy(final String string) {
    return LEGACY.deserialize(string);
  }

  public static Component reverseMiniMessage(final Component component) {
    return plain(MINI_MESSAGE.serialize(component));
  }

  public static Component miniMessage(final Component component) {
    return miniMessage(plain(component));
  }

  public static Component miniMessage(final String string) {
    return MINI_MESSAGE.deserialize(string);
  }

  public static Component plain(final String string) {
    return PLAIN.deserialize(string);
  }

  public static String plain(final Component component) {
    return PLAIN.serialize(component);
  }

}
