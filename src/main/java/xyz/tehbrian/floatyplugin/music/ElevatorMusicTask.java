package xyz.tehbrian.floatyplugin.music;

import com.google.inject.Inject;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import xyz.tehbrian.floatyplugin.FloatyPlugin;
import xyz.tehbrian.floatyplugin.user.User;
import xyz.tehbrian.floatyplugin.user.UserService;

@SuppressWarnings("ClassCanBeRecord")
public final class ElevatorMusicTask {

    public static final int FALL_DISTANCE_MIN = 150;

    private static final @NonNull Key SOUND_KEY = Key.key("floating", "music.elevator");
    private static final @NonNull Sound SOUND = Sound.sound(SOUND_KEY, Sound.Source.MUSIC, 1, 1);
    private static final @NonNull SoundStop SOUND_STOP = SoundStop.named(SOUND_KEY);

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

    public void start() {
        final Server server = this.floatyPlugin.getServer();

        server.getScheduler().scheduleSyncRepeatingTask(this.floatyPlugin, () -> {
            for (final Player player : server.getOnlinePlayers()) {
                final User user = this.userService.getUser(player);

                if (player.getFallDistance() > FALL_DISTANCE_MIN) {
                    if (!user.elevatorMusicPlaying()) {
                        player.playSound(SOUND);
                        user.elevatorMusicPlaying(true);
                    }
                } else {
                    if (user.elevatorMusicPlaying()) {
                        player.stopSound(SOUND_STOP);
                        user.elevatorMusicPlaying(false);
                    }
                }
            }
        }, 1, 20);
    }

}