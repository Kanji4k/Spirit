package client.confis.spirit;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.MinecraftClient;

/** Branding: replaces "Confis0oooooo" in the top-left of the Right Shift menu. */
public final class SpiritBranding {
    public static final String NAME = "Spirit";
    public static final String FULL_NAME = "Spirit Client";
    public static final String CREDIT = "Create By Confis0oooooo";

    /** Call this where ConfisScreen / ConfigScreen used to draw the "Confis0oooooo" title. */
    public static void drawHeader(DrawContext ctx, MinecraftClient mc, int x, int y, int accent) {
        ctx.drawText(mc.textRenderer, NAME, x, y, accent, true);
        // small credit text: drawn at 0.6x scale under the title
        var m = ctx.getMatrices();
        m.pushMatrix();
        m.translate(x, y + 11);
        m.scale(0.6f, 0.6f);
        ctx.drawText(mc.textRenderer, CREDIT, 0, 0, 0xFFAAAAAA, false);
        m.popMatrix();
    }
    private SpiritBranding() {}
}
