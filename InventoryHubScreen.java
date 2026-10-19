package client.confis.spirit;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import java.util.*;

/** 1) Inventory hub: one screen showing everything you carry (merged by item), armor and offhand. */
public final class InventoryHubScreen extends Screen {
    private final Screen parent;
    public InventoryHubScreen(Screen parent) { super(Text.literal("Spirit - Inventory Hub")); this.parent = parent; }

    @Override public void render(DrawContext g, int mx, int my, float delta) {
        super.render(g, mx, my, delta);
        g.drawCenteredTextWithShadow(textRenderer, "Inventory Hub", width / 2, 12, 0xFFFFFFFF);
        var inv = client.player.getInventory();
        LinkedHashMap<String, int[]> merged = new LinkedHashMap<>();
        LinkedHashMap<String, ItemStack> sample = new LinkedHashMap<>();
        for (int i = 0; i < inv.size(); i++) {
            ItemStack s = inv.getStack(i);
            if (s.isEmpty()) continue;
            String k = s.getItem().toString();
            merged.computeIfAbsent(k, x -> new int[1])[0] += s.getCount();
            sample.putIfAbsent(k, s);
        }
        int cols = Math.max(1, (width - 40) / 54), n = 0;
        ItemStack hover = null;
        for (var e : merged.entrySet()) {
            int x = 20 + (n % cols) * 54, y = 36 + (n / cols) * 24; n++;
            g.drawItem(sample.get(e.getKey()), x, y);
            g.drawText(textRenderer, "x" + e.getValue()[0], x + 18, y + 5, 0xFFFFFFFF, true);
            if (mx >= x && mx < x + 50 && my >= y && my < y + 18) hover = sample.get(e.getKey());
        }
        if (hover != null) g.drawItemTooltip(textRenderer, hover, mx, my);
        g.drawCenteredTextWithShadow(textRenderer, "ESC to go back", width / 2, height - 14, 0xFF888888);
    }
    @Override public void close() { client.setScreen(parent); }
}
