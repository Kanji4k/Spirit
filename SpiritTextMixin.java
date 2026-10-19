package client.confis.mixin;

import client.confis.spirit.SpiritBranding;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Renames "Confis0oooooo" -> "Spirit" wherever the menu draws it, and adds the small credit line. */
@Mixin(DrawContext.class)
public abstract class SpiritTextMixin {
    @ModifyVariable(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private String spirit$renameStr(String s) { return rename(s); }

    @ModifyVariable(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Text spirit$renameText(Text t) {
        String s = t.getString();
        if (s.equals("Confis0oooooo")) return Text.literal(SpiritBranding.NAME).setStyle(t.getStyle());
        if (s.equals("Confis0oooooo Tools")) return Text.literal("Spirit Tools").setStyle(t.getStyle());
        return t;
    }

    @Inject(method = "drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V", at = @At("TAIL"))
    private void spirit$credit(TextRenderer tr, Text t, int x, int y, int color, boolean shadow, CallbackInfo ci) {
        if (!t.getString().equals(SpiritBranding.NAME)) return;
        DrawContext g = (DrawContext) (Object) this;
        var m = g.getMatrices();
        m.pushMatrix();
        m.translate(x, y + 11);
        m.scale(0.6f, 0.6f);
        g.drawText(tr, SpiritBranding.CREDIT, 0, 0, 0xFFAAAAAA, false);
        m.popMatrix();
    }

    private static String rename(String s) {
        if ("Confis0oooooo".equals(s)) return SpiritBranding.NAME;
        if ("Confis0oooooo Tools".equals(s)) return "Spirit Tools";
        return s;
    }
}
