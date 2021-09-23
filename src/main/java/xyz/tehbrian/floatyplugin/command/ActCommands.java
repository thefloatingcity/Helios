package xyz.tehbrian.floatyplugin.command;

import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.Inject;
import dev.tehbrian.tehlib.paper.cloud.PaperCloudCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.config.LangConfig;

public class ActCommands extends PaperCloudCommand<CommandSender> {

    private final LangConfig langConfig;

    @Inject
    public ActCommands(
            final @NonNull LangConfig langConfig
    ) {
        this.langConfig = langConfig;
    }

    /**
     * Register the command.
     *
     * @param commandManager the command manager
     */
    @Override
    public void register(@NonNull final PaperCommandManager<CommandSender> commandManager) {
//@CommandAlias("launch")
//    @CommandPermission("floatyplugin.action.launch")
//    @Description("Like a rocket!")
//    @CommandCompletion("@player")
//    public void onLaunch(final Player player, @Optional @CommandPermission("floatyplugin.action.launchother") final OnlinePlayer target) {
//        final Player targetPlayer = target == null ? player : target.getPlayer();
//
//        targetPlayer.setVelocity(new Vector(0, 10, 0));
//        targetPlayer.getWorld().playSound(targetPlayer.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, SoundCategory.MASTER, 5, 0.75F);
//
//        if (target == null) {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("action.launch_self").formats(player.getDisplayName()).build());
//        } else {
//            Bukkit.broadcastMessage(new MsgBuilder()
//                    .msgKey("action.launch_other")
//                    .formats(player.getDisplayName(), targetPlayer.getDisplayName())
//                    .build());
//        }
//    }
//
//    @CommandAlias("boost")
//    @CommandPermission("floatyplugin.action.boost")
//    @Description("Gives you a case of the zoomies.")
//    @CommandCompletion("@players")
//    public void onBoost(final Player player, @Optional @CommandPermission("floatyplugin.action.boostother") final OnlinePlayer target) {
//        final Player targetPlayer = target == null ? player : target.getPlayer();
//
//        targetPlayer.setVelocity(targetPlayer.getLocation().getDirection().multiply(3));
//        targetPlayer.getWorld().playSound(
//                targetPlayer.getEyeLocation(),
//                Sound.ENTITY_FIREWORK_ROCKET_LAUNCH,
//                SoundCategory.MASTER,
//                5,
//                0.75F
//        );
//
//        if (target == null) {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("action.boost_self").formats(player.getDisplayName()).build());
//        } else {
//            Bukkit.broadcastMessage(new MsgBuilder()
//                    .msgKey("action.boost_other")
//                    .formats(player.getDisplayName(), targetPlayer.getDisplayName())
//                    .build());
//        }
//    }
//
//    @CommandAlias("zap")
//    @CommandPermission("floatyplugin.action.zap")
//    @Description("Kentucky Fried Player")
//    @CommandCompletion("@players")
//    public void onZap(final Player player, @Optional @CommandPermission("floatyplugin.action.zapother") final OnlinePlayer target) {
//        final Player targetPlayer = target == null ? player : target.getPlayer();
//
//        targetPlayer.getWorld().strikeLightning(targetPlayer.getLocation());
//
//        if (target == null) {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("action.zap_self").formats(player.getDisplayName()).build());
//        } else {
//            Bukkit.broadcastMessage(new MsgBuilder()
//                    .msgKey("action.zap_other")
//                    .formats(player.getDisplayName(), targetPlayer.getDisplayName())
//                    .build());
//        }
//    }
//
//    @CommandAlias("poke")
//    @CommandPermission("floatyplugin.action.poke")
//    @Description("Useful for annoying others.")
//    @CommandCompletion("@players")
//    public void onPoke(final Player player, @Optional @CommandPermission("floatyplugin.action.pokeother") final OnlinePlayer target) {
//        final Player targetPlayer = target == null ? player : target.getPlayer();
//
//        final double maxY = this.main.getConfig().getDouble("poke_force.maxY");
//        final double minY = this.main.getConfig().getDouble("poke_force.minY");
//        final double maxXZ = this.main.getConfig().getDouble("poke_force.maxXZ");
//        final double minXZ = this.main.getConfig().getDouble("poke_force.minXZ");
//        final Random random = new Random();
//        final double randX = minXZ + random.nextDouble() * (maxXZ - minXZ);
//        final double randY = minY + random.nextDouble() * (maxY - minY);
//        final double randZ = minXZ + random.nextDouble() * (maxXZ - minXZ);
//        final Vector randomVector = new Vector(randX, randY, randZ);
//
//        targetPlayer.setVelocity(randomVector);
//
//        if (target == null) {
//            Bukkit.broadcastMessage(new MsgBuilder().msgKey("action.poke_self").formats(player.getDisplayName()).build());
//        } else {
//            Bukkit.broadcastMessage(new MsgBuilder()
//                    .msgKey("action.poke_other")
//                    .formats(player.getDisplayName(), targetPlayer.getDisplayName())
//                    .build());
//        }
//    }
    }

}
