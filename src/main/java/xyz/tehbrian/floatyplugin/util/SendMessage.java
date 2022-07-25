package xyz.tehbrian.floatyplugin.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.List;

public final class SendMessage {

  private SendMessage() {
  }

  /**
   * shut up. ik the name isn't correct in any way, but it feels like I'm making
   * a DSL and that's cool.
   *
   * @param audience   the audience to send the messages to
   * @param components the messages to send
   */
  public static void s(final Audience audience, final List<Component> components) {
    for (final Component component : components) {
      audience.sendMessage(component);
    }
  }

}
