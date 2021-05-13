package xyz.tehbrian.tfcplugin.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.guis.MenuGui;

@SuppressWarnings("unused")
@CommandAlias("menu")
@Description("Collection of useful tidbits.")
public class MenuCommand extends BaseCommand {

    @Default
    public void onMenu(final Player player) {
        MenuGui.generate().show(player);
    }
}
