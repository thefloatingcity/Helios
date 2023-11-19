package city.thefloating.floatyplugin.config;

import city.thefloating.floatyplugin.ChatFormat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Objects;

public final class BookDeserializer {

  private BookDeserializer() {
  }

  public static Component deserializePage(final ConfigurationNode book, final Integer pageNumber) {
    final int pageIndex = pageNumber - 1;

    final List<? extends ConfigurationNode> pages = book.node("pages").childrenList();
    final ConfigurationNode page = pages.get(pageIndex);

    Component finalComponent = Component.empty();

    finalComponent = finalComponent.append(MiniMessage.miniMessage().deserialize(
        book.node("multistart").getString() + book.node("page-header").getString(),
        TagResolver.resolver(
            Placeholder.unparsed("title", Objects.requireNonNull(page.node("title").getString())),
            Placeholder.unparsed("page", pageNumber.toString()),
            Placeholder.unparsed("page_count", String.valueOf(pages.size()))
        )
    ));

    final String multi = book.node("multi").getString();
    try {
      for (final String line : Objects.requireNonNull(page.node("content").getList(String.class))) {
        finalComponent = finalComponent
            .append(Component.newline())
            .append(ChatFormat.miniMessage(multi + line));
      }
    } catch (final SerializationException e) {
      return Component.empty();
    }

    return finalComponent;
  }

  public static int pageCount(final ConfigurationNode book) {
    return book.node("pages").childrenList().size();
  }

}
