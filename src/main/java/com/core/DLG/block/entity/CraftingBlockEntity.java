package com.core.DLG.block.entity;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import com.core.DLG.configs.ItemConfig;
import com.core.DLG.enums.QualityEnum;
import com.core.DLG.enums.TypeEnum;
import com.core.DLG.inventory.CraftingBlockMenu;
import com.core.DLG.item.RegistryItem;
import com.core.DLG.util.EntryHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class CraftingBlockEntity extends BlockEntity implements MenuProvider {
    private ItemStack renderStack = ItemStack.EMPTY; // 要渲染的物品
    private float rotation = 0.0f; // 物品旋转角度
    private float hoverHeight = 0.0f; // 物品悬浮高度

    // 添加输入输出物品栏
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (slot < 3) {
                updateOutput();
            }
        };
    };

    // 创建物品栏
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public CraftingBlockEntity(BlockPos pos, BlockState state) {
        super(RegistryBlockEntity.DEBRIS_SMITHING_TABLE_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.dlg.debris_smithing_table");
    }

    // 玩家交互时,服务器创建菜单实例
    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) {
        return new CraftingBlockMenu(
            id, 
            inventory, 
            ContainerLevelAccess.create(Objects.requireNonNull(level), worldPosition), 
            this
        );
    }

    /**
     * 提供访问接口(其他容器是否能与之交互,例如漏斗)
     * @param cap  能力类型 (时容器访问还是什么)
     * @param side 交互方向
     * @return     能力接口
    */
    @Override
    public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    // 初始化,确保方块实体加载后加载能力(延迟初始化)
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    // 销毁时,使能力失效
    @Override
    public void invalidateCaps() {
        super.invalidateCaps(); // 调用父类方法,销毁能力
        lazyItemHandler.invalidate(); // 销毁插槽
    }

    // 保存物品栏数据(确保数据持久化,例如:将物品保存在工作方块中)
    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        super.saveAdditional(tag);
    }

    // 加载物品栏数据
    @Override
    public void load(@Nonnull CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("inventory"));  // 解析数据
        updateOutput(); // 加载时更新输出
    }
    
    // 更新输出
    private void updateOutput() {
        ItemStack input1 = itemHandler.getStackInSlot(0);
        ItemStack input2 = itemHandler.getStackInSlot(1);
        ItemStack input3 = itemHandler.getStackInSlot(2);
        ItemStack output = calculateOutput(input1, input2, input3); // 计算输出
        itemHandler.setStackInSlot(3, output); // 设置输出槽

        this.renderStack = input1.copy();
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    // 根据输入计算输出
    private ItemStack calculateOutput(ItemStack input1, ItemStack input2, ItemStack input3) {
        // 情况1: 绑定词条
        if (!input1.isEmpty() && isDebris(input2) && input3.isEmpty()) {
            return bindDebris(input1, input2);
        }
        // 情况2: 提升碎片品质
        if (input1.isEmpty() && isDebris(input2) && !input3.isEmpty()) {
            return upgradeDebrisQuality(input2, input3);
        }
        // 情况3: 提升装备等级
        if (hasDebris(input1) && input2.isEmpty() && !input3.isEmpty()) {
            return upgradeItemLevel(input1, input3);
        }
        // 情况4: 换 slot
        if (hasDebris(input1) && !input2.isEmpty() && input3.isEmpty()) {
            return switchSlot(input1, input2);
        }
        return ItemStack.EMPTY;
    }

    // 消耗输入物品逻辑
    public void consumeInputs() {
        ItemStack input1 = itemHandler.getStackInSlot(0);
        ItemStack input2 = itemHandler.getStackInSlot(1);
        ItemStack input3 = itemHandler.getStackInSlot(2);

        if (!input1.isEmpty() && isDebris(input2) && input3.isEmpty()) {
            input1.shrink(1);
            input2.shrink(1);
        } else if (input1.isEmpty() && isDebris(input2) && !input3.isEmpty()) {
            input2.shrink(1);
            input3.shrink(1);
        } else if (hasDebris(input1) && input2.isEmpty() && !input3.isEmpty()) {
            int consumeCount = countConsumption(input1,input3);
            input1.shrink(1);
            input3.shrink(consumeCount);
        } else if (hasDebris(input1) && !input2.isEmpty() && input3.isEmpty()) {
            input1.shrink(1);
        }
        setChanged();
        updateOutput();
    }

    //#region 物品处理
    // 检查物品是否为碎片 √
    private boolean isDebris(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() == RegistryItem.EQUIPMENT_DEBRIS.get();
    }

    // 检查物品是否绑定了碎片 √
    public static boolean hasDebris(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null &&  // 物品存在标签
            tag.contains("boundDebris") && // 包含绑定碎片标签
            !tag.getCompound("boundDebris").isEmpty() &&  // 绑定碎片标签不为空
            tag.getCompound("boundDebris").getBoolean("bound"); // 绑定碎片标签中包含碎片数据
    }

    // 绑定词条 √
    private ItemStack bindDebris(ItemStack item, ItemStack debris) {
        ItemStack result = item.copy();
        CompoundTag tag = result.getOrCreateTag();
        CompoundTag debrisTag = debris.getTag();
        if (hasDebris(item)) return ItemStack.EMPTY; // 已绑定碎片不可再绑定
        if (debrisTag == null) return ItemStack.EMPTY; // 碎片无标签不可绑定
        CompoundTag newTag = debrisTag.copy();

        // 获取碎片品质
        int debrisQuality = debrisTag.getInt("quality");
        if (debrisQuality < 0) return ItemStack.EMPTY;
        debrisQuality = debrisQuality == 0 ? 3 : debrisQuality;

        // 设置绑定碎片数据
        newTag.putBoolean("bound", true);
        newTag.putInt("level", 0);
        newTag.putInt("maxLevel", debrisQuality * 5);
        newTag.putInt("exp", 0);
        newTag.putInt("maxExp", 100);
        tag.put("boundDebris", newTag);

        if (debrisQuality > 6) tag.putBoolean("Unbreakable", true); // 传说及以上品质装备无损坏
        
        try {
            EntryHelper.initEntry(
                tag, 
                TypeEnum.getType(debrisTag.getString("type")), 
                QualityEnum.getQuality(debrisTag.getInt("quality"))
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        EntryHelper.transitDefaultAttribute(result, tag);
        result.setCount(1);
        return result;
    }

    // 提升碎片品质 √
    private ItemStack upgradeDebrisQuality(ItemStack debris, ItemStack material) {
        try {
            ItemConfig.init();
        } catch (Exception e) {
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
        if (!ItemConfig.upgradeDetect(debris, material)) return ItemStack.EMPTY;
        ItemStack result = debris.copy();
        CompoundTag tag = result.getOrCreateTag();
        int currentQuality = tag.getInt("quality");
        tag.putInt("quality", currentQuality + 1);
        return result;
    }

    // 提升装备等级 √
    private ItemStack upgradeItemLevel(ItemStack item, ItemStack material) {
        if (!hasDebris(item)) return ItemStack.EMPTY; // 未绑定碎片不可升级
        ItemStack result = item.copy();
        CompoundTag tag = result.getOrCreateTag();
        CompoundTag debrisTag = tag.getCompound("boundDebris");
        int currentLevel = debrisTag.getInt("level");
        int maxLevel = debrisTag.getInt("maxLevel");
        int currentExp = debrisTag.getInt("exp");
        int maxExp = debrisTag.getInt("maxExp");
        int expGain = getExpGain(material); // 每个材料提供的经验

        if (currentLevel >= maxLevel) return ItemStack.EMPTY;
        if (expGain == -1) return ItemStack.EMPTY;

        int consumeCount = countConsumption(item, material);
        int newExp = currentExp + expGain * consumeCount; // 新经验值初始化
        int newLevel = currentLevel; // 新等级初始化
        while (newExp >= maxExp) {
            newExp -= maxExp;
            newLevel++;
            maxExp = (newLevel+1)*100;
        }

        newLevel = Math.min(newLevel, maxLevel); // 不超过最大等级
        debrisTag.putInt("level", newLevel);
        debrisTag.putInt("exp", newExp);
        debrisTag.putInt("maxExp", maxExp);

        EntryHelper.upgradeEntry(tag);

        return result;
    }

    // 替换槽位 √
    private ItemStack switchSlot(ItemStack item, ItemStack switchMaterial) {
        ItemStack result = item.copy();
        CompoundTag tag = result.getTag();

        String slot = "mainhand";
        String newType = null;
        switch (ForgeRegistries.ITEMS.getKey(switchMaterial.getItem()).toString()) {
            case "minecraft:stick":
                slot = "mainhand";
                break;
            case "minecraft:spider_eye":
                slot = "head";
                newType = "helmet";
                break;
            case "minecraft:rotten_flesh":
                slot = "chest";
                newType = "chestplate";
                break;
            case "minecraft:bone":
                slot = "legs";
                newType = "leggings";
                break;
            case "minecraft:string":
                slot = "feet";
                newType = "boots";
                break;
            default:
                return ItemStack.EMPTY;
        }

        if (tag != null && tag.contains("AttributeModifiers")) {
            ListTag modifiersList = tag.getList("AttributeModifiers", 10);
            if (tag.contains("boundDebris")) {
                for (int i = 0; i < modifiersList.size(); i++) {
                    CompoundTag modifierTag = modifiersList.getCompound(i);
                    modifierTag.putString("Slot", slot);
                }
            }
            CompoundTag debrisTag = tag.getCompound("boundDebris");
            String type = debrisTag.getString("type");
            type = newType == null ? type : newType;
            debrisTag.putString("type", type);
            return result;
        }
        
        return ItemStack.EMPTY;
    }

    // 计算升级材料消耗
    private int countConsumption(ItemStack item, ItemStack material) {
        ItemStack result = item.copy();
        CompoundTag tag = result.getOrCreateTag();
        CompoundTag debrisTag = tag.getCompound("boundDebris");
        int currentExp = debrisTag.getInt("exp");
        int maxExp = debrisTag.getInt("maxExp");
        int expGain = getExpGain(material); // 每个材料提供的经验
        if (expGain == -1) return 0;

        int count = material.getCount(); // 获取材料数量
        int consumeCount = 0; // 初始化消耗数量
        consumeCount = (int) Math.ceil((maxExp - currentExp)/(float) expGain); // 计算消耗数量
        if (consumeCount > count) consumeCount = count; // 如果消耗数量大于材料数量，则消耗数量为材料数量
        
        return consumeCount;
    }

    // 获取材料经验值
    private int getExpGain(ItemStack itemStack) { 
        try {
            ItemConfig.init();
            return ItemConfig.getExp(itemStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    //#endregion

    //#region 渲染相关

    // 获取渲染物品
    public ItemStack getRenderStack() {
        return renderStack;
    }

    // 获取旋转角度
    public float getRotation(float partialTicks) {
        return rotation + partialTicks;
    }

    // 获取悬浮高度
    public float getHoverHeight(float partialTicks) {
        return (float) Math.sin((hoverHeight + partialTicks) * 0.1f) * 0.1f + 0.2f;
    }

    // 每tick更新旋转和悬浮动画
    public void tick() {
        if (!renderStack.isEmpty()) {
            rotation += 1.0f;
            if (rotation >= 360.0f) {
                rotation -= 360.0f;
            }
            hoverHeight += 1.0f;
        }
    }
    //#endregion
}
