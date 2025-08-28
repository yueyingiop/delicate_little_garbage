package com.core.DLG.mixin.itemStack;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.core.DLG.configs.ItemConfig;
import com.core.DLG.util.ItemBreakHelper;
import com.core.DLG.util.format.RGBSettings;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Mutable
    @Shadow
    @Final
    public static Codec<ItemStack> CODEC;

    @Shadow
    private int count;
    
    //#region 物品堆叠
    @Inject(method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V",at = @At(value = "TAIL"))
    private void read(CompoundTag nbt, CallbackInfo ci){
        if(nbt.contains("countMod")){
            this.count = nbt.getInt("countMod");
        }
    }
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void clinit(CallbackInfo ci) throws IOException {
        ItemConfig.init();
        CODEC = RecordCodecBuilder.create((instance)->{
            return instance.group(
                // BuiltInRegistries.ITEM.byNameCodec().fieldOf("id").forGetter(ItemStack::getItem),
                // ExtraCodecs.intRange(1, Integer.MAX_VALUE).fieldOf("Count").orElse(1).forGetter(ItemStack::getCount),
                ForgeRegistries.ITEMS.getCodec().fieldOf("id").forGetter(ItemStack::getItem),
                Codec.INT.fieldOf("Count").forGetter(ItemStack::getCount),
                CompoundTag.CODEC.optionalFieldOf("tag").forGetter((stack) -> {
                    return Optional.ofNullable(stack.getTag());
                }),
                Codec.INT.optionalFieldOf("countMod").forGetter((opt) -> {
                    return Optional.of(opt.getCount());
                })
            ).apply(instance, (item, count, tag, newCount) -> {
                int oldCount = count;
                if(newCount.isPresent()){
                    oldCount = newCount.get();
                }
                ItemStack stack = new ItemStack(item, oldCount);
                return stack;
            });
        });
    }

    @Redirect(method = "save",at = @At(value = "INVOKE", target ="Lnet/minecraft/nbt/CompoundTag;putByte(Ljava/lang/String;B)V"))
    private void write(CompoundTag instance, String key, byte value){
        instance.putByte(key,value);
        instance.putInt("countMod",this.count);
    }

    @Inject(method = "getMaxStackSize", at = @At("RETURN"), cancellable = true)
    private void getMaxStack(CallbackInfoReturnable<Integer> cir) throws IOException { 
        ItemConfig.init();
        cir.setReturnValue(ItemConfig.getMaxStackSize());
    }
    //#endregion

    //#region 物品损坏事件
    @Inject(
        method = "hurtAndBreak",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
        ),
        cancellable = true
    )
    private <T extends LivingEntity> void onHurtAndBreak(int amount, T entity, Consumer<T> breakCallback, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        if (!entity.level().isClientSide && (entity instanceof Player player)) {
            if (ItemConfig.getItemBrokenDrops()) {
                ItemBreakHelper.handleItemBreak(player, stack);
                stack.shrink(1);
                ci.cancel();
            }
        }
    }
    //#endregion

    //#region 物品名称显示
    @Inject(method = "getHoverName", at = @At("RETURN"), cancellable = true)
    private void onGetHoverName(CallbackInfoReturnable<Component> cir) {
        Component original = cir.getReturnValue();
        String text = original.getString();
        
        // 检查是否包含颜色格式
        if (text.contains("#")) {
            MutableComponent formattedComponent = Component.literal("");
            List<RGBSettings.ParsedSegment> segments = RGBSettings.parseText(text);
            
            for (RGBSettings.ParsedSegment segment : segments) {
                MutableComponent part = Component.literal(segment.text());
                segment.color().ifPresent(color -> {
                    part.withStyle(style -> style.withColor(color));
                });
                formattedComponent.append(part);
            }
            
            cir.setReturnValue(formattedComponent);
        }
    }
    //#endregion
}
