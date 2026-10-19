package client.confis.spirit;

import client.confis.Module;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/** Second entrypoint: runs next to the original Confis entrypoint inside the same jar. */
public class SpiritClient implements ClientModInitializer {
    static KeyBinding HUB;

    @Override public void onInitializeClient() {
        ZoomFreecam.init();
        ShulkerTooltip.register();
        SpiritUsers.refresh();
        registerModules();
        HUB = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.spirit.hub", GLFW.GLFW_KEY_H, KeyBinding.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (HUB.wasPressed()) if (mc.player != null) mc.setScreen(new InventoryHubScreen(mc.currentScreen));
        });
    }

    /** The original Modules list is compiled, so add our modules to it by reflection. */
    @SuppressWarnings("unchecked")
    static void registerModules() {
        try {
            Class<?> c = Class.forName("client.confis.Modules");
            for (Field f : c.getDeclaredFields()) {
                if (!Modifier.isStatic(f.getModifiers()) || !List.class.isAssignableFrom(f.getType())) continue;
                f.setAccessible(true);
                List<Module> list = new ArrayList<>((List<Module>) f.get(null));
                list.removeIf(m -> "effects".equals(idOf(m)));           // swap text effect timer for icon version
                list.add(new EffectTimerIcons());
                list.add(new CountModules.TotemCount());
                list.add(new CountModules.PotionCount());
                list.add(new CountModules.Saturation());
                list.add(new CountModules.CrosshairIndicator());
                f.set(null, list);
                return;
            }
        } catch (Throwable t) {
            System.err.println("[Spirit] could not register modules into Confis: " + t);
        }
    }

    static String idOf(Module m) {
        try { Field f = Module.class.getDeclaredField("id"); f.setAccessible(true); return (String) f.get(m); }
        catch (Throwable t) { return ""; }
    }
}
