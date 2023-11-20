package city.thefloating.floatyplugin;

public final class Permission {

  public static final String ROOT = "floatyplugin";

  // build within a world's spawn.
  public static final String BUILD_SPAWN = ROOT + ".build-spawn";

  // build in or transpose to a realm.
  public static final String REALM = ROOT + ".realm";
  public static final String REALM_MADLANDS = REALM + ".madlands";
  public static final String REALM_OVERWORLD = REALM + ".overworld";
  public static final String REALM_NETHER = REALM + ".nether";
  public static final String REALM_END = REALM + ".end";
  public static final String REALM_BACKROOMS = REALM + ".backrooms";

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
