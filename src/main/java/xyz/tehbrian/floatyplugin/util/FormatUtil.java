package xyz.tehbrian.floatyplugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public final class FormatUtil {

    public static final @NotNull LegacyComponentSerializer LEGACY_WITH_URLS = LegacyComponentSerializer.builder().extractUrls().build();
    public static final @NotNull LegacyComponentSerializer LEGACY = LegacyComponentSerializer.legacyAmpersand();
    public static final @NotNull PlainTextComponentSerializer PLAIN = PlainTextComponentSerializer.plainText();
    public static final @NotNull MiniMessage MINI_MESSAGE = MiniMessage.get();

    private FormatUtil() {
    }

    public static @NonNull Component legacyWithUrls(final @NonNull Component component) {
        return LEGACY_WITH_URLS.deserialize(plain(component));
    }

    public static @NonNull Component reverseLegacy(final @NonNull Component component) {
        return plain(LEGACY.serialize(component));
    }

    public static @NonNull Component legacy(final @NonNull Component component) {
        return legacy(plain(component));
    }

    public static @NonNull Component legacy(final @NonNull String string) {
        return LEGACY.deserialize(string);
    }

    public static @NonNull Component reverseMiniMessage(final @NonNull Component component) {
        return plain(MINI_MESSAGE.serialize(component));
    }

    public static @NonNull Component miniMessage(final @NonNull Component component) {
        return miniMessage(plain(component));
    }

    public static @NonNull Component miniMessage(final @NonNull String string) {
        return MINI_MESSAGE.parse(string);
    }

    public static @NonNull Component plain(final @NonNull String string) {
        return plain(string);
    }

    public static @NonNull String plain(final @NonNull Component component) {
        return PLAIN.serialize(component);
    }

}
