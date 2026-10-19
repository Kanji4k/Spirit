package client.confis.mixin;

import client.confis.spirit.ZoomFreecam;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 7) Zoom: divides the FOV while the zoom key is held. */
@Mixin(GameRenderer.class)
public class SpiritZoomMixin {
    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void spirit$zoom(Camera camera, float tickProgress, boolean changingFov, CallbackInfoReturnable<Float> cir) {
        if (ZoomFreecam.zooming()) cir.setReturnValue(cir.getReturnValue() / ZoomFreecam.zoomFactor);
    }
}
