package com.core.DLG.inventory;

import javax.annotation.Nonnull;

import com.core.DLG.block.entity.CraftingBlockEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CraftingBlockMenu extends AbstractContainerMenu {

    private final CraftingBlockEntity blockEntity;
    private final IItemHandler playerInventory;
    private final ContainerLevelAccess access;
    private final boolean isPortable; // 新增：是否为便携模式
    private final ItemStackHandler portableInventory; // 新增：便携模式下的虚拟库存

    protected CraftingBlockMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, ContainerLevelAccess.NULL, extraData != null ? (CraftingBlockEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()) : null);
    }

    public CraftingBlockMenu(int id, Inventory inventory, ContainerLevelAccess access, CraftingBlockEntity blockEntity) {
        super(RegistryMenu.CRAFTING_BLOCK_MENU.get(), id);
        this.blockEntity = blockEntity; // 获取方块实体
        this.playerInventory = new InvWrapper(inventory); // 获取玩家物品栏
        this.access = access; // 获取容器位置
        this.isPortable = false;
        this.portableInventory = null;
        setupSlots();
        // if (blockEntity != null) {
        //     // 添加方块物品栏槽位
        //     blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(h -> {
        //         addSlot(new SlotItemHandler(h, 0, 30, 35) {
        //             @Override
        //             public int getMaxStackSize(ItemStack stack) {
        //                 return 1;
        //             }
        //             @Override
        //             public int getMaxStackSize(){
        //                 return 1;
        //             }
        //         }); // 输入槽1
        //         addSlot(new SlotItemHandler(h, 1, 48, 35) {
        //             @Override
        //             public int getMaxStackSize(ItemStack stack) {
        //                 return 1;
        //             }
        //             @Override
        //             public int getMaxStackSize(){
        //                 return 1;
        //             }
        //         }); // 输入槽2
        //         addSlot(new SlotItemHandler(h, 2, 66, 35)); // 输入槽3
        //         addSlot(new SlotItemHandler(h, 3, 124, 35) {
        //             // 输出槽不允许放入物品
        //             @Override
        //             public boolean mayPlace(ItemStack stack) {
        //                 return false;
        //             }

        //             // 输出槽点击时消耗输入槽物品
        //             @Override
        //             public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
        //                 blockEntity.consumeInputs();
        //                 super.onTake(player, stack);
        //             }
        //         }); // 输出槽
        //     });
        // }

        // layoutPlayerInventorySlots(8, 84);
    }

    public CraftingBlockMenu(int id, Inventory inventory) {
        super(RegistryMenu.CRAFTING_BLOCK_MENU.get(), id);
        this.blockEntity = null;
        this.playerInventory = new InvWrapper(inventory);
        this.access = ContainerLevelAccess.NULL;
        this.isPortable = true;
        this.portableInventory = new ItemStackHandler(4); // 4个槽位：3输入 + 1输出
        setupSlots();
    }

    private void setupSlots() {
        IItemHandler itemHandler = getItemHandler();

        if (itemHandler != null) {
            // 添加方块物品栏槽位
            
            addSlot(new SlotItemHandler(itemHandler, 0, 30, 35) {
                @Override
                public int getMaxStackSize(ItemStack stack) {
                    return 1;
                }
                @Override
                public int getMaxStackSize(){
                    return 1;
                }
            }); // 输入槽1
            addSlot(new SlotItemHandler(itemHandler, 1, 48, 35) {
                @Override
                public int getMaxStackSize(ItemStack stack) {
                    return 1;
                }
                @Override
                public int getMaxStackSize(){
                    return 1;
                }
            }); // 输入槽2
            addSlot(new SlotItemHandler(itemHandler, 2, 66, 35)); // 输入槽3
            addSlot(new SlotItemHandler(itemHandler, 3, 124, 35) {
                // 输出槽不允许放入物品
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return false;
                }

                // 输出槽点击时消耗输入槽物品
                @Override
                public void onTake(@Nonnull Player player, @Nonnull ItemStack stack) {
                    consumeInputs();
                    super.onTake(player, stack);
                }
            }); // 输出槽

        }

        layoutPlayerInventorySlots(8, 84);
    }

    private IItemHandler getItemHandler() {
        System.out.println(isPortable);
        if (isPortable) {
            return portableInventory;
        } else if (blockEntity != null) {
            return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().orElse(null);
        } else {
            return new ItemStackHandler(4);
        }
    }

    private void consumeInputs() {
        if (isPortable) {
            // 便携模式下的输入消耗逻辑
            for (int i = 0; i < 3; i++) {
                portableInventory.extractItem(i, 1, false);
            }
        } else if (blockEntity != null) {
            blockEntity.consumeInputs();
        }
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // 玩家物品栏
        addSlotBox(playerInventory, 9, leftCol, topRow, 18, 18, 9, 3);
        // 快捷栏
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    // 添加插槽盒
    private int addSlotBox(IItemHandler handler, int index, int x, int y, int dx, int dy, int horAmount, int verAmount) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    // 添加插槽行
    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    // 快捷移动物品
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY; // 空物品
        Slot slot = this.slots.get(index); // 获取点击的槽位
        // 如果槽位不为空且有物品
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem(); // 获取槽位中的物品
            itemstack = stack.copy(); // 复制物品用于返回
            // 尝试移动物品
            if (index < 4) {
                if (!this.moveItemStackTo(stack, 4, 40, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }
        return itemstack;
    }

    // 容器是否仍然有效(判断容器是否还在等等)
    @Override
    public boolean stillValid(@Nonnull Player player) {
        if (isPortable) {
            return true; // 便携模式总是有效
        }
        if (blockEntity != null) {
            return stillValid(access, player, blockEntity.getBlockState().getBlock());
        }
        return false;
    }
    
}
