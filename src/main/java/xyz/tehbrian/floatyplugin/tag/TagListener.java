package xyz.tehbrian.floatyplugin.tag;

import com.google.inject.Inject;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.NodePath;
import xyz.tehbrian.floatyplugin.config.LangConfig;

@SuppressWarnings("ClassCanBeRecord")
public final class TagListener implements Listener {

    private final TagService tagService;
    private final LangConfig langConfig;

    @Inject
    public TagListener(
            final @NonNull TagService tagService,
            final @NonNull LangConfig langConfig
    ) {
        this.tagService = tagService;
        this.langConfig = langConfig;
    }

    @EventHandler
    public void onPunch(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player damager
                && event.getEntity() instanceof Player victim
                && this.tagService.isPlaying(damager)
                && this.tagService.isPlaying(victim)
                && this.tagService.getIt().equals(damager)) {
            this.tagService.setIt(victim);
            victim.sendMessage(this.langConfig.c(NodePath.path("tag", "now_it")));
            victim.playSound(victim.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5F);
            damager.sendMessage(this.langConfig.c(NodePath.path("tag", "tagged_player"), Template.of("player", victim.displayName())));
            damager.playSound(damager.getEyeLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1, 0.7F);
        }
    }

    @EventHandler
    public void onGameModeChange(final PlayerGameModeChangeEvent event) {
        if (this.tagService.isPlaying(event.getPlayer())
                && event.getNewGameMode() != GameMode.ADVENTURE) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(this.langConfig.c(NodePath.path("tag", "adventure_only")));
        }
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        this.tagService.setPlaying(event.getPlayer(), false);
    }

}