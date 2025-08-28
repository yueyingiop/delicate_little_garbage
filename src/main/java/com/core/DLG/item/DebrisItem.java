package com.core.DLG.item;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.util.ColorHelper;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class DebrisItem extends Item {
    private final String itemId;
    public DebrisItem(String name, Properties properties) {
        super(properties);
        this.itemId = name;
    }

    // 获取物品ID
    public String getItemId() {
        return this.itemId;
    }

    // 创建默认实例
    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", TypeEnum.UNDEFINED_TYPE.getString());
        nbt.putInt("quality", QualityEnum.UNDEFINED_QUALITY.getNumber());
        stack.setTag(nbt);
        return stack;
    }

    // 创建物品实例
    public ItemStack setDebrisTag(TypeEnum type, QualityEnum quality) {
        ItemStack stack = new ItemStack(this);
        CompoundTag nbt = new CompoundTag();
        nbt.putString("type", type.getString());
        nbt.putInt("quality", quality.getNumber());
        stack.setTag(nbt);
        return stack;
    }

    // tooltip提示
    @Override
    public void appendHoverText(@Nonnull ItemStack itemStack, @Nullable Level level, @Nonnull List<Component> components, @Nonnull TooltipFlag tooltip) {
        super.appendHoverText(itemStack, level, components, tooltip);
        if (itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getTag();
            if (nbt == null) return;
            if (nbt.contains("type")) {
                String type = nbt.getString("type");
                TypeEnum typeEnum = TypeEnum.getType(type);
                components.add(
                    Component.translatable(
                        "tooltip.dlg.debris.type", 
                        Component.translatable("tooltip.dlg.debris.type." + type)
                            .withStyle(Style.EMPTY.withColor(typeEnum.getColor()))
                    ).withStyle(ChatFormatting.GOLD)
                );
            }
            if (nbt.contains("quality")) {
                int quality = nbt.getInt("quality");
                QualityEnum qualityEnum = QualityEnum.getQuality(quality);
                String qualityString = qualityEnum.getString();
                int color = qualityEnum.getNumber() == QualityEnum.QUALITY9.getNumber()? ColorHelper.createDynamicColor() : qualityEnum.getColor();
                components.add(
                    Component.translatable(
                        "tooltip.dlg.debris.quality", 
                        Component.translatable("tooltip.dlg.debris.quality." + qualityString)
                            .withStyle(Style.EMPTY.withColor(color))
                    ).withStyle(ChatFormatting.GOLD)
                );
            }
        }
    }

    // 能否放入砂轮
    @Override
    public boolean canGrindstoneRepair(ItemStack stack) {
        return true;
    }
}
