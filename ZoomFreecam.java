package client.confis.spirit;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

/** 7) Zoom (hold C) with scroll-wheel adjust, 14) Freecam (press F6). Call init() from ConfisClient. */
public final class ZoomFreecam {
    public static KeyBinding ZOOM, FREECAM;
    public static float zoomFactor = 4.0f;           // FOV divisor while zooming
    public static boolean freecam;
    public static Vec3d camPos = Vec3d.ZERO;
    public static float camYaw, camPitch;

    public static boolean zooming() { return ZOOM != null && ZOOM.isPressed(); }

    public static void init() {
        ZOOM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.spirit.zoom", GLFW.GLFW_KEY_C, KeyBinding.Category.MISC));
        FREECAM = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.spirit.freecam", GLFW.GLFW_KEY_F6, KeyBinding.Category.MISC));
        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            if (mc.player == null) { freecam = false; return; }
            while (FREECAM.wasPressed()) toggle(mc);
            if (freecam) move(mc);
        });
    }

    static void toggle(MinecraftClient mc) {
        freecam = !freecam;
        if (freecam) { camPos = mc.gameRenderer.getCamera().getPos(); camYaw = mc.player.getYaw(); camPitch = mc.player.getPitch(); }
    }

    static void move(MinecraftClient mc) {
        double speed = 0.6;
        var o = mc.options;
        double yawR = Math.toRadians(camYaw);
        Vec3d fwd = new Vec3d(-Math.sin(yawR), 0, Math.cos(yawR));
        Vec3d right = new Vec3d(-fwd.z, 0, fwd.x);
        Vec3d d = Vec3d.ZERO;
        if (o.forwardKey.isPressed()) d = d.add(fwd);
        if (o.backKey.isPressed())    d = d.subtract(fwd);
        if (o.rightKey.isPressed())   d = d.subtract(right);
        if (o.leftKey.isPressed())    d = d.add(right);
        if (o.jumpKey.isPressed())    d = d.add(0, 1, 0);
        if (o.sneakKey.isPressed())   d = d.add(0, -1, 0);
        camPos = camPos.add(d.multiply(speed));
        // keep the real player still while the camera flies
        o.forwardKey.setPressed(false); o.backKey.setPressed(false); o.leftKey.setPressed(false);
        o.rightKey.setPressed(false); o.jumpKey.setPressed(false); o.sneakKey.setPressed(false);
    }
    private ZoomFreecam() {}
}
