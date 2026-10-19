package client.confis.spirit;

import client.confis.Config;
import client.confis.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderPipelines;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;
import java.util.*;

/** 3) Replacement for ExtraModules.EffectTimerModule: shows the effect ICON + time instead of the name. */
public final class EffectTimerIcons extends Module {
    public EffectTimerIcons() { super("effects", "Effect Timer", "HUD", 0.01, 0.12); }

    @Override public int width(MinecraftClient mc) { return 18 + mc.textRenderer.getWidth("00:00") + 6; }
    @Override public int height(MinecraftClient mc) {
        return Math.max(1, mc.player == null ? 2 : mc.player.getStatusEffects().size()) * 20;
    }

    @Override public void draw(DrawContext g, MinecraftClient mc, Config.ModuleCfg cfg, int x, int y, boolean preview) {
        List<StatusEffectInstance> list = new ArrayList<>();
        if (mc.player != null) list.addAll(mc.player.getStatusEffects());
        list.sort(Comparator.comparingInt(StatusEffectInstance::getDuration).reversed());
        int row = 0;
        for (StatusEffectInstance e : list) {
            Identifier sprite = InGameHud.getEffectTexture(e.getEffectType());   // the same icon vanilla uses
            g.drawGuiTexture(RenderPipelines.GUI_TEXTURED, sprite, x + 1, y + row * 20 + 1, 18, 18);
            int secs = e.isInfinite() ? -1 : Math.max(0, e.getDuration() / 20);
            String t = secs < 0 ? "inf" : String.format("%02d:%02d", secs / 60, secs % 60);
            int col = (secs >= 0 && secs < 10) ? 0xFFFF5555 : 0xFFFFFFFF;   // expiring colour
            g.drawText(mc.textRenderer, t, x + 22, y + row * 20 + 6, col, true);
            if (e.getAmplifier() > 0)
                g.drawText(mc.textRenderer, "" + (e.getAmplifier() + 1), x + 14, y + row * 20 + 12, 0xFFFFFF55, true);
            row++;
        }
    }
}
