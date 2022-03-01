package xyz.tehbrian.floatyplugin.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class BookDeserializer {

    private BookDeserializer() {
    }

    public static List<Component> deserializePage(final @NonNull CommentedConfigurationNode book, final @NonNull Integer pageNumber) {
        final int pageIndex = pageNumber - 1;

        final List<CommentedConfigurationNode> pages = Objects.requireNonNull(book).node("pages").childrenList();
        final CommentedConfigurationNode page = pages.get(pageIndex);

        final List<Component> messages = new ArrayList<>();

        messages.add(MiniMessage.miniMessage().deserialize(
                book.node("multistart").getString() + book.node("page_header").getString(),
                TagResolver.resolver(
                        Placeholder.unparsed("title", Objects.requireNonNull(page.node("title").getString())),
                        Placeholder.unparsed("page", pageNumber.toString()),
                        Placeholder.unparsed("page_count", String.valueOf(pages.size()))
                )
        ));

        final String multi = book.node("multi").getString();
        try {
            for (final String line : Objects.requireNonNull(page.node("content").getList(String.class))) {
                messages.add(FormatUtil.miniMessage(multi + line));
            }
        } catch (final SerializationException e) {
            e.printStackTrace();
            return List.of();
        }

        return messages;
    }

    public static int pageCount(final @NonNull CommentedConfigurationNode book) {
        return Objects.requireNonNull(book).node("pages").childrenList().size();
    }

}
