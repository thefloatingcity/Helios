package xyz.tehbrian.floatyplugin.music;

import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

@SuppressWarnings("ClassCanBeRecord")
public class ElevatorMusicTask implements Runnable {

    private final FloatyPlugin floatyPlugin;
    private final UserService userService;

    @Inject
    public ElevatorMusicTask(
            final @NonNull FloatyPlugin floatyPlugin,
            final @NonNull UserService userService
    ) {
        this.floatyPlugin = floatyPlugin;
        this.userService = userService;
    }

    @Override
    public void run() {
        for (final Player player : this.floatyPlugin.getServer().getOnlinePlayers()) {
            final User user = this.userService.getUser(player);

            if (player.getFallDistance() > 150) {
                if (!user.elevatorMusicPlaying()) {
                    player.playSound(Sound.sound(
                            Key.key("floating", "music.elevator"),
                            Sound.Source.MUSIC, 1, 1
                    ));
                    user.elevatorMusicPlaying(true);
                }
            } else {
                if (user.elevatorMusicPlaying()) {
                    player.stopSound(SoundStop.named(Key.key("floating", "music.elevator")));
                    user.elevatorMusicPlaying(false);
                }
            }
        }
    }

}
