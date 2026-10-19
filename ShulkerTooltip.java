package client.confis.spirit;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ContainerComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;

/** 10) Shulker box tooltip: lists contents (merged by item) in the hover tooltip. Call register() once. */
public final class ShulkerTooltip {
    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, ctx, type, lines) -> {
            ContainerComponent box = stack.get(DataComponentTypes.CONTAINER);
            if (box == null) return;
            Map<Text, Integer> merged = new LinkedHashMap<>();
            for (ItemStack s : box.iterateNonEmpty()) merged.merge(s.getName(), s.getCount(), Integer::sum);
            if (merged.isEmpty()) return;
            lines.add(Text.literal("Contents:").formatted(Formatting.GRAY));
            int shown = 0;
            for (var e : merged.entrySet()) {
                if (shown++ >= 12) { lines.add(Text.literal("  ...and more").formatted(Formatting.DARK_GRAY)); break; }
                lines.add(Text.literal("  " + e.getValue() + "x ").formatted(Formatting.YELLOW).append(e.getKey()));
            }
        });
    }
    private ShulkerTooltip() {}
}
