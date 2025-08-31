package com.core.DLG.mixin.client;

import java.util.List;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.util.format.RGBSettings;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(Font.class)
public abstract class FontMixin {

    @Shadow
    public abstract float renderText(
        String text, 
        float x, float y, 
        int color, 
        boolean shadow, 
        Matrix4f matrix, 
        MultiBufferSource buffer, 
        Font.DisplayMode mode, 
        int backgroundColor, 
        int packedLight
    );

    @Unique
    private boolean dlg$processing = false;

    // 绘制文本
    @Inject(
        method = "renderText(Ljava/lang/String;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)F", 
        at = @At("HEAD"), 
        cancellable = true
    )
    private void onRenderText(
        String text, 
        float x, float y, 
        int color, 
        boolean shadow, 
        Matrix4f matrix, 
        MultiBufferSource buffer, 
        Font.DisplayMode mode, 
        int backgroundColor, 
        int packedLight, 
        CallbackInfoReturnable<Float> cir
    ) {
        if (dlg$processing) {
            return;
        }
        if (text != null && text.contains("#")){
            dlg$processing = true;
            try {
                List<RGBSettings.ParsedSegment> segments = RGBSettings.parseText(text);
                float currentX = x;
                for (RGBSettings.ParsedSegment segment : segments) {
                    float width = renderText(
                        segment.text(), 
                        currentX, y, 
                        segment.color().orElse(color), 
                        shadow, matrix, buffer, mode, 
                        backgroundColor, packedLight
                    );
                    currentX = width;
                }
                
                cir.setReturnValue(currentX - x);
                cir.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dlg$processing = false;
            }
        }
    }

}
