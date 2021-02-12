package xyz.tehbrian.tfcplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import xyz.tehbrian.tfcplugin.TFCPlugin;
import xyz.tehbrian.tfcplugin.util.ConfigUtils;
import xyz.tehbrian.tfcplugin.util.msg.MsgBuilder;

@SuppressWarnings("unused")
public class VoidLoopListener implements Listener {

    private final TFCPlugin main;

    public VoidLoopListener(TFCPlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onEntityDamageByVoid(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.VOID) return;
        Location location = event.getEntity().getLocation();

        if (location.getY() > -50) return;
        event.setCancelled(true);

        if (location.getY() > -450) return;
        Bukkit.getScheduler().runTask(main, () -> {
            location.setY(650);
            event.getEntity().teleport(location);

            if (!(event.getEntity() instanceof Player)) return;
            Player player = (Player) event.getEntity();

            if (player.getFallDistance() >= 3000) {
                player.sendMessage(new MsgBuilder().prefixKey("prefixes.warper.prefix").msgKey("msg.warp.max").build());
                player.setFallDistance(0);
                player.teleport(ConfigUtils.getSpawn());
                player.getWorld().strikeLightningEffect(ConfigUtils.getSpawn());
                player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, ConfigUtils.getSpawn(), 1);
                player.getWorld().playSound(ConfigUtils.getSpawn(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4, 1);
            } else if (player.getFallDistance() >= 2000) {
                player.sendMessage(new MsgBuilder().prefixKey("prefixes.warper.prefix").msgKey("msg.warp.second").build());
            } else if (player.getFallDistance() >= 1000) {
                player.sendMessage(new MsgBuilder().prefixKey("prefixes.warper.prefix").msgKey("msg.warp.first").build());
            }
        });
    }
}
