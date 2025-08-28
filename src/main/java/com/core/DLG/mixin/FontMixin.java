package com.core.DLG.mixin;

import java.lang.reflect.Method;
import java.util.List;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.util.format.RGBSettings;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;

@Mixin(Font.class)
public abstract class FontMixin {
    @Unique
    private static Method renderTextMethod;

    static {
        try {
            // 使用反射获取 renderText 方法
            renderTextMethod = Font.class.getDeclaredMethod(
                "renderText", 
                String.class, Float.TYPE, Float.TYPE, Integer.TYPE, Boolean.TYPE,
                Matrix4f.class, MultiBufferSource.class, Font.DisplayMode.class,
                Integer.TYPE, Integer.TYPE
            );
            renderTextMethod.setAccessible(true); // 设置可访问
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

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
        if (text != null && text.contains("#")){
            List<RGBSettings.ParsedSegment> segments = RGBSettings.parseText(text);
            float currentX = x;
            try {
                for (RGBSettings.ParsedSegment segment : segments) {
                    // 使用反射调用 renderText 方法
                    currentX = (Float) renderTextMethod.invoke(
                        (Font) (Object) this,
                        segment.text(), 
                        currentX, y, 
                        segment.color().orElse(color), 
                        shadow, matrix, buffer, mode, 
                        backgroundColor, packedLight
                    );
                }
                
                cir.setReturnValue(currentX - x);
                cir.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
