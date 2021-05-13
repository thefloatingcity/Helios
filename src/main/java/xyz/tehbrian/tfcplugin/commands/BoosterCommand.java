package xyz.tehbrian.tfcplugin.commands;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.Axis;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.booster.BoosterManager;

@CommandAlias("booster")
@CommandPermission("tfcplugin.booster")
@Description("Various commands for manipulating boosters.")
public class BoosterCommand extends BaseCommand {

    private final TFCPlugin main;

    public BoosterCommand(TFCPlugin main) {
        this.main = main;
    }

    @Subcommand("create")
    @Description("Create a booster.")
    @CommandCompletion("*")
    public void onCreate(Player player, String name, Integer radius, Axis axis) {
        BoosterManager boosterManager = this.main.getBoosterManager();

        boosterManager.newBooster(name, player.getLocation(), radius, axis);
        boosterManager.getBooster(name).generate();
    }

    @HelpCommand
    public void onHelp(CommandSender sender, CommandHelp help) {
        help.showHelp();
    }
}
