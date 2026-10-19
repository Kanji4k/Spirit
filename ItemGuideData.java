package client.confis.spirit;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import java.util.*;

/**
 * 8) Item guide data: crafting recipe (from the server recipe manager, singleplayer/integrated) and
 * "where to find" text from a bundled hint table. Use from ItemGuideScreen when an item is clicked.
 */
public final class ItemGuideData {
    /** Extend this table freely: item id -> where to find it. */
    public static final Map<String, String> FIND = Map.ofEntries(
        Map.entry("minecraft:diamond", "Mine diamond ore below Y=16 (best Y=-59)."),
        Map.entry("minecraft:netherite_ingot", "Smelt 4 netherite scrap + 4 gold ingots. Scrap: ancient debris at Y=15 in the Nether."),
        Map.entry("minecraft:totem_of_undying", "Dropped by Evokers (woodland mansions, raids)."),
        Map.entry("minecraft:elytra", "End ships in the End cities."),
        Map.entry("minecraft:ender_pearl", "Dropped by Endermen."),
        Map.entry("minecraft:blaze_rod", "Dropped by Blazes in Nether fortresses."),
        Map.entry("minecraft:shulker_shell", "Dropped by Shulkers in End cities.")
    );

    public static String whereToFind(Item item) {
        return FIND.getOrDefault(Registries.ITEM.getId(item).toString(), "Check loot tables / mob drops (no hint added yet).");
    }

    /**
     * TODO (needs your 1.21.11 mappings): list crafting recipes for the item.
     * In 1.21.2+ recipes moved to RecipeDisplayEntry (client recipe book) and the server RecipeManager,
     * and the exact accessor names changed between builds, so I did not guess them here.
     * Plan: singleplayer -> server.getRecipeManager().values(), keep entries whose output matches `item`,
     * draw the ingredient grid in ItemGuideScreen. Multiplayer -> use player.getRecipeBook() displays.
     */
    public static List<String> recipes(Item item) {
        return List.of("Recipe lookup not wired yet.");
    }
    private ItemGuideData() {}
}
