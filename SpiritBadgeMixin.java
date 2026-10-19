package client.confis.mixin;

import client.confis.spirit.SpiritUsers;
import client.confis.spirit.TierTagger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** 4) "C" badge + 9) tier tag in front of names (nametag above head and tab list use getDisplayName). */
@Mixin(PlayerEntity.class)
public abstract class SpiritBadgeMixin {
    @Inject(method = "getDisplayName", at = @At("RETURN"), cancellable = true)
    private void spirit$badge(CallbackInfoReturnable<Text> cir) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (!(self instanceof AbstractClientPlayerEntity)) return;
        String me = MinecraftClient.getInstance().getSession().getUsername();
        String name = self.getGameProfile().name();
        Text out = cir.getReturnValue();
        String tier = TierTagger.tierOf(name);
        if (!tier.isEmpty() && !tier.equals("-"))
            out = Text.literal("[" + tier + "] ").formatted(Formatting.GOLD).append(out);
        if (SpiritUsers.isUser(name, me))
            out = Text.literal("C ").formatted(Formatting.AQUA, Formatting.BOLD).append(out);
        cir.setReturnValue(out);
    }
}
