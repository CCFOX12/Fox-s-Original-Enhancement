package vplus.client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.minecraft.world.entity.player.Player;
import vplus.client.cosmetic.SkinComposite;

@Mixin(AvatarRenderer.class)
public abstract class AvatarSkinMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
	private void vplus$skins(Avatar entity, AvatarRenderState state, float partialTick, CallbackInfo ci) {
		if (entity instanceof Player player) {
			state.setData(SkinComposite.PIECES, SkinComposite.read(player));
			state.setData(SkinComposite.ON_ARMOR, vplus.cosmetic.CosmeticLayer.armor(player));
		} else {
			state.setData(SkinComposite.PIECES, java.util.List.of());
		}
	}

	@Inject(method = "getTextureLocation(Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;)Lnet/minecraft/resources/Identifier;", at = @At("RETURN"), cancellable = true)
	private void vplus$composite(AvatarRenderState state, CallbackInfoReturnable<Identifier> cir) {
		Identifier composite = SkinComposite.texture(state, cir.getReturnValue());
		if (composite != null) {
			cir.setReturnValue(composite);
		}
	}
}
