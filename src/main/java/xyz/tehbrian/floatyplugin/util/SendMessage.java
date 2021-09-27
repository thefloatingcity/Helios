package xyz.tehbrian.floatyplugin.util;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.List;

/**
 * shut up ik the name isn't correct in any way but it feels like i'm making
 * a dsl and that's cool
 */
public final class SendMessage {

    private SendMessage() {
    }

    public static void s(final Audience audience, final List<Component> components) {
        for (final Component component : components) {
            audience.sendMessage(component);
        }
    }

}
