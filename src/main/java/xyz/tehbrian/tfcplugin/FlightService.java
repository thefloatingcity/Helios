package xyz.tehbrian.tfcplugin;

import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.tfcplugin.user.User;
import xyz.tehbrian.tfcplugin.user.UserService;

public class FlightService {

    private final UserService userService;
    private final FloatyPlugin floatyPlugin;

    public FlightService(
            final @NonNull UserService userService,
            final @NonNull FloatyPlugin floatyPlugin
    ) {
        this.userService = userService;
        this.floatyPlugin = floatyPlugin;
    }

    public void checkFlight(final Player player) {
        final User user = this.userService.getUser(player);

        if (player.hasPermission("tfcplugin.core.fly") && user.flyBypassEnabled()) {
            this.enableFlight(player);
        } else {
            this.disableFlight(player);
        }
    }

    public void enableFlight(final Player player) {
        this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
            player.setAllowFlight(true);
        });
    }

    public void disableFlight(final Player player) {
        this.floatyPlugin.getServer().getScheduler().runTask(this.floatyPlugin, () -> {
            player.setAllowFlight(false);
            player.setFlying(false);
        });
    }

}
