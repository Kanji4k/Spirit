package client.confis.spirit;

import client.confis.Config;
import client.confis.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.entity.player.PlayerInventory;

/** 2) Totem count, 16) Potion count, 6) Saturation, 11) Crosshair indicator. */
public final class CountModules {

    static int count(MinecraftClient mc, java.util.function.Predicate<ItemStack> p) {
        if (mc.player == null) return 0;
        PlayerInventory inv = mc.player.getInventory();
        int n = 0;
        for (int i = 0; i < inv.size(); i++) { ItemStack s = inv.getStack(i); if (p.test(s)) n += s.getCount(); }
        return n;
    }

    /** Generic icon + number box. */
    abstract static class IconCount extends Module {
        IconCount(String id, String title, double x, double y) { super(id, title, "HUD", x, y); }
        abstract ItemStack icon();
        abstract int value(MinecraftClient mc);
        @Override public int width(MinecraftClient mc) { return 22 + mc.textRenderer.getWidth("" + 99); }
        @Override public int height(MinecraftClient mc) { return 20; }
        @Override public void draw(DrawContext g, MinecraftClient mc, Config.ModuleCfg cfg, int x, int y, boolean preview) {
            g.drawItem(icon(), x + 2, y + 2);
            g.drawText(mc.textRenderer, "" + (preview ? 3 : value(mc)), x + 22, y + 6, 0xFFFFFFFF, true);
        }
    }

    public static final class TotemCount extends IconCount {
        public TotemCount() { super("totems", "Totem Count", 0.01, 0.30); }
        ItemStack icon() { return new ItemStack(Items.TOTEM_OF_UNDYING); }
        int value(MinecraftClient mc) { return count(mc, s -> s.isOf(Items.TOTEM_OF_UNDYING)); }
    }

    public static final class PotionCount extends IconCount {
        public PotionCount() { super("potions", "Potion Count", 0.01, 0.36); }
        ItemStack icon() { return new ItemStack(Items.SPLASH_POTION); }
        int value(MinecraftClient mc) {
            return count(mc, s -> s.isOf(Items.POTION) || s.isOf(Items.SPLASH_POTION) || s.isOf(Items.LINGERING_POTION));
        }
    }

    public static final class Saturation extends Module {
        public Saturation() { super("saturation", "Saturation", "HUD", 0.01, 0.42); }
        @Override public int width(MinecraftClient mc) { return 70; }
        @Override public int height(MinecraftClient mc) { return 12; }
        @Override public void draw(DrawContext g, MinecraftClient mc, Config.ModuleCfg cfg, int x, int y, boolean preview) {
            float sat = preview ? 5.0f : (mc.player == null ? 0 : mc.player.getHungerManager().getSaturationLevel());
            g.drawText(mc.textRenderer, "Saturation: " + String.format("%.1f", sat), x + 2, y + 2, 0xFFFFD27F, true);
        }
    }

    /** Crosshair indicator: tiny ring showing attack cooldown + red tint when target is in reach. */
    public static final class CrosshairIndicator extends Module {
        public CrosshairIndicator() { super("crosshair", "Crosshair Indicator", "HUD", 0.5, 0.5); }
        @Override public boolean movable() { return false; }
        @Override public int width(MinecraftClient mc) { return 0; }
        @Override public int height(MinecraftClient mc) { return 0; }
        @Override public void draw(DrawContext g, MinecraftClient mc, Config.ModuleCfg cfg, int x, int y, boolean preview) {
            if (mc.player == null) return;
            int cx = mc.getWindow().getScaledWidth() / 2, cy = mc.getWindow().getScaledHeight() / 2;
            float cd = mc.player.getAttackCooldownProgress(0f);
            boolean onEntity = mc.targetedEntity != null;
            int col = onEntity ? 0xFFFF4444 : 0xFFFFFFFF;
            int len = Math.round(cd * 12);
            g.fill(cx - 6, cy + 9, cx - 6 + len, cy + 10, col);   // cooldown bar under the crosshair
            if (onEntity) g.fill(cx - 1, cy - 1, cx + 1, cy + 1, col);
        }
    }
    private CountModules() {}
}
