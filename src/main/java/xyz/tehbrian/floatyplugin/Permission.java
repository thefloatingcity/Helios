package xyz.tehbrian.floatyplugin;

public final class Permission {

  public static final String ROOT = "floatyplugin";

  // build in a world.
  public static final String BUILD = ROOT + ".build";
  public static final String BUILD_MADLANDS = BUILD + ".madlands";
  public static final String BUILD_OVERWORLD = BUILD + ".overworld";
  public static final String BUILD_NETHER = BUILD + ".nether";
  public static final String BUILD_END = BUILD + ".end";
  public static final String BUILD_BACKROOMS = BUILD + ".backrooms";

  // build in a world's spawn.
  // purposefully heterogeneous to prevent `floatyplugin.build.*` allowing building in spawn.
  public static final String BUILD_SPAWN = ROOT + ".build-spawn";

  // go to a world.
  public static final String WORLD = ROOT + ".world";
  public static final String WORLD_MADLANDS = WORLD + ".madlands";
  public static final String WORLD_OVERWORLD = WORLD + ".overworld";
  public static final String WORLD_NETHER = WORLD + ".nether";
  public static final String WORLD_END = WORLD + ".end";
  public static final String WORLD_BACKROOMS = WORLD + ".backrooms";

  public static final String MILK = ROOT + ".milk";
  public static final String FLY = ROOT + ".fly";
  public static final String BROADCAST = ROOT + ".broadcast";
  public static final String RELOAD = ROOT + ".reload";
  public static final String GAMEMODE = ROOT + ".gamemode";
  public static final String HAT = ROOT + ".hat";
  public static final String PIANO = ROOT + ".piano";
  public static final String CHAT_COLOR = ROOT + ".chat-color";

  public static final String ACT = ROOT + ".act";
  public static final String ZAP = ACT + ".zap";
  public static final String POKE = ACT + ".poke";

  public static final String EMOTE = ROOT + ".emote";
  public static final String UNREADABLE = EMOTE + ".unreadable";
  public static final String SHRUG = EMOTE + ".shrug";
  public static final String SPOOK = EMOTE + ".spook";
  public static final String HUG = EMOTE + ".hug";
  public static final String SMOOCH = EMOTE + ".smooch";
  public static final String BLAME = EMOTE + ".blame";
  public static final String HIGHFIVE = EMOTE + ".highfive";
  public static final String SUE = EMOTE + ".sue";

  private Permission() {
  }

}
