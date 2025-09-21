package com.core.DLG.item;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import com.core.DLG.entity.CloudWhaleEntity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeSpawnEggItem;

public class CloudWhaleSpawnEggItem extends ForgeSpawnEggItem {

    public CloudWhaleSpawnEggItem(
        Supplier<? extends EntityType<? extends Mob>> type, 
        int backgroundColor,
        int highlightColor, 
        Properties props
    ) {
        super(type, backgroundColor, highlightColor, props);
    }

    @Override
    public Optional<Mob> spawnOffspringFromSpawnEgg(
        @Nonnull Player player, 
        @Nonnull Mob mob, 
        @Nonnull EntityType<? extends Mob> entityType,
        @Nonnull ServerLevel level, 
        @Nonnull Vec3 pos, 
        @Nonnull ItemStack stack
    ) {
        Optional<Mob> offspringOptional = super.spawnOffspringFromSpawnEgg(player, mob, entityType, level, pos, stack);

        // 如果实体生成成功，进行额外处理
        if (offspringOptional.isPresent()) {
            Mob offspring = offspringOptional.get();
            
            if (offspring instanceof CloudWhaleEntity cloudWhale) {
                // 确保云鲸正确初始化
                cloudWhale.refreshDimensions();
                
                // 给幼年云鲸一个初始的向上速度
                if (cloudWhale.isBaby()) {
                    cloudWhale.setDeltaMovement(0, 0.1, 0);
                }
            }
            
            return Optional.of(offspring);
        }
        
        return Optional.empty();
    }

}
