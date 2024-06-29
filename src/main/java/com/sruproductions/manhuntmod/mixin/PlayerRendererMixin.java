//package com.sruproductions.manhuntmod.mixin;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.sruproductions.manhuntmod.client.model.CustomPlayerModel;
//import com.sruproductions.manhuntmod.client.renderer.CustomPlayerRenderer;
//import net.minecraft.client.model.PlayerModel;
//import net.minecraft.client.model.geom.ModelLayers;
//import net.minecraft.client.player.AbstractClientPlayer;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.player.PlayerRenderer;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.util.Mth;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Overwrite;
//import org.spongepowered.asm.mixin.Shadow;
//
//@Mixin(PlayerRenderer.class)
//public abstract class PlayerRendererMixin extends CustomPlayerRenderer {
//
//    @Shadow
//    public abstract ResourceLocation getTextureLocation(AbstractClientPlayer player);
//
//    public PlayerRendererMixin(EntityRendererProvider.Context context) {
//        super(context, false);
//        this.model = new CustomPlayerModel(context.bakeLayer(ModelLayers.PLAYER));
//    }
//
//    @Overwrite
//    public void render(AbstractClientPlayer player, float p_115456_, float p_115457_, PoseStack poseStack, net.minecraft.client.renderer.MultiBufferSource buffer, int packedLight) {
//        float f = Mth.rotLerp(p_115457_, player.yBodyRotO, player.yBodyRot);
//        float f1 = Mth.rotLerp(p_115457_, player.yHeadRotO, player.yHeadRot);
//        float f2 = f1 - f;
//        if (player.isPassenger() && player.getVehicle() instanceof LivingEntity) {
//            LivingEntity livingentity = (LivingEntity) player.getVehicle();
//            f = Mth.rotLerp(p_115457_, livingentity.yBodyRotO, livingentity.yBodyRot);
//            f2 = f1 - f;
//            float f3 = Mth.wrapDegrees(f2);
//            if (f3 < -85.0F) {
//                f3 = -85.0F;
//            }
//
//            if (f3 >= 85.0F) {
//                f3 = 85.0F;
//            }
//
//            f = f1 - f3;
//            if (f3 * f3 > 2500.0F) {
//                f += f3 * 0.2F;
//            }
//
//            f2 = f1 - f;
//        }
//
//        this.setupRotations(player, poseStack, player.getViewYRot(p_115457_), p_115456_, p_115457_);
//        this.applyRotations(player, poseStack, f2, p_115457_);
//        super.render(player, p_115456_, p_115457_, poseStack, buffer, packedLight);
//    }
//}
