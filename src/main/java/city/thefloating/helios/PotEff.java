package city.thefloating.helios;

import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Shorthands for working with {@link PotionEffect}.
 */
public final class PotEff {

  public static final int INF = PotionEffect.INFINITE_DURATION;

  private PotEff() {
  }

  // ambient: dimmer particles if particles are enabled.
  // particles: whether to show particles.
  // icon: whether to show top right icon. players can still see in inventory.

  public static PotionEffect hidden(final PotionEffectType type, final int duration, final int amplifier) {
    return new PotionEffect(type, duration, amplifier, true, false, false);
  }

  public static PotionEffect visible(final PotionEffectType type, final int duration, final int amplifier) {
    return new PotionEffect(type, duration, amplifier, false, true, true);
  }

  public static PotionEffect of(
      final PotionEffectType type,
      final int duration,
      final int amplifier,
      final boolean ambient,
      final boolean particles,
      final boolean icon
  ) {
    return new PotionEffect(type, duration, amplifier, ambient, particles, icon);
  }

}
