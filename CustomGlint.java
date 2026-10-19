package client.confis.spirit;

/**
 * 12) Custom glint - settings holder. The glint colour is applied at render time; this needs a
 * mixin into the item/armor glint render layer, whose target differs in each 1.21.x build, so it
 * is left as the settings + the hook point to wire to your mappings.
 */
public final class CustomGlint {
    public static boolean enabled = true;
    public static int argb = 0xFFB066FF;      // purple by default
    public static boolean rainbow = false;
    public static int current() {
        if (!rainbow) return argb;
        float h = (System.currentTimeMillis() % 4000L) / 4000f;
        return 0xFF000000 | java.awt.Color.HSBtoRGB(h, 0.7f, 1f) & 0xFFFFFF;
    }
    private CustomGlint() {}
}
