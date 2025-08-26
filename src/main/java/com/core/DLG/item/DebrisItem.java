package com.core.DLG.item;

import com.core.DLG.enums.TypeEnum;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class DebrisItem extends Item {
    CompoundTag nbt = new CompoundTag();
    private final String itemId;
    public DebrisItem(String name, Properties properties) {
        super(properties);
        this.itemId = name;
    }

    public String getItemId() {
        return this.itemId;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        nbt.putString("type", TypeEnum.SWORD.getString());
        stack.setTag(nbt);
        return stack;
    }

    // 创建物品实例
    public ItemStack setDebrisTag(TypeEnum type) {
        ItemStack stack = new ItemStack(this);
        nbt.putString("type", type.getString());
        stack.setTag(nbt);
        return stack;
    }
}
