package city.thefloating.helios.milk;

import broccolai.corn.paper.item.special.PotionBuilder;
import city.thefloating.helios.PotEff;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public final class Milk {

  public static final PotionEffectType EFFECT = PotionEffectType.DOLPHINS_GRACE;
  public static final int AMPLIFIER = 5;

  private Milk() {
  }

  public static ItemStack regular() {
    return base()
        .name(Component.text("Potion of Milk"))
        .build();
  }

  public static ItemStack splash() {
    return base()
        .name(Component.text("Splash Potion of Milk"))
        .material(Material.SPLASH_POTION)
        .build();
  }

  private static PotionBuilder base() {
    return PotionBuilder.ofType(Material.POTION)
        .loreList(
            Component.text("Milk XXXIV").color(NamedTextColor.BLUE),
            Component.empty(),
            Component.text("Bottled at your local femboy hooters.").color(NamedTextColor.GRAY)
        )
        .addCustomEffect(PotEff.hidden(EFFECT, 100, AMPLIFIER), true)
        .addFlag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ITEM_SPECIFICS)
        .color(Color.WHITE);
  }

}
