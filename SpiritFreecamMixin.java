package client.confis.mixin;

import client.confis.spirit.ZoomFreecam;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.gen.Invoker;

/** 14) Freecam: after vanilla positions the camera, move it to the free-flying position. */
@Mixin(Camera.class)
public abstract class SpiritFreecamMixin {
    @Invoker("setPos") abstract void spirit$setPos(double x, double y, double z);
    @Invoker("setRotation") abstract void spirit$setRot(float yaw, float pitch);

    @Inject(method = "update", at = @At("TAIL"))
    private void spirit$freecam(BlockView area, Entity focused, boolean thirdPerson, boolean inverseView, float tickProgress, CallbackInfo ci) {
        if (!ZoomFreecam.freecam) return;
        spirit$setPos(ZoomFreecam.camPos.x, ZoomFreecam.camPos.y, ZoomFreecam.camPos.z);
        spirit$setRot(ZoomFreecam.camYaw, ZoomFreecam.camPitch);
    }
}
