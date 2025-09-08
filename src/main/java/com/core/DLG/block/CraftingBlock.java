package com.core.DLG.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.core.DLG.block.entity.CraftingBlockEntity;
import com.core.DLG.block.entity.RegistryBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class CraftingBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private final VoxelShape CUSTOM_COLLISION_SHAPE;
    private final String blockId;

    public CraftingBlock(String name, Properties properties) {
        this(name, properties, null);
    }

    public CraftingBlock(String name, Properties properties, VoxelShape customCollisionShape) {
        super(properties);
        this.blockId = name;
        // 自定义碰撞箱
        this.CUSTOM_COLLISION_SHAPE = (customCollisionShape == null? Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D): customCollisionShape);
        // 设置默认朝向
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(
        @Nonnull BlockState state, 
        @Nonnull Level level, 
        @Nonnull BlockPos pos, 
        @Nonnull Player player, 
        @Nonnull InteractionHand hand, 
        @Nonnull BlockHitResult hit
    ) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof CraftingBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, (CraftingBlockEntity) entity, pos);
            }
            // else {
            //     throw new IllegalStateException("Our Container provider is missing!");
            // }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    // 方块实体
    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos blockPos, @Nonnull BlockState state) {
        return RegistryBlockEntity.DEBRIS_SMITHING_TABLE_ENTITY.get().create(blockPos, state);
    }

    // 渲染类型
    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    // 创建方块状态
    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    // 放置时朝向玩家
    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    // 旋转
    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    // 镜像
    @Override
    public BlockState mirror(@Nonnull BlockState state, @Nonnull Mirror mirror) {
       return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    // 自定义碰撞箱
    @Override
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return CUSTOM_COLLISION_SHAPE;
    }

    // 自定义视觉轮廓
    @Override
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext contex) {
        return CUSTOM_COLLISION_SHAPE;
    }

    // 获取方块ID
    public String getItemId() {
        return this.blockId;
    }

}
