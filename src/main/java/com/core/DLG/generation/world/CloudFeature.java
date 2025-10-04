package com.core.DLG.generation.world;

import javax.annotation.Nonnull;

import com.core.DLG.block.RegistryBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;


// 特征类
public class CloudFeature extends Feature<NoneFeatureConfiguration> {

    public CloudFeature() {
        super(NoneFeatureConfiguration.CODEC); // 配置特征
    }

    @Override
    public boolean place(@Nonnull FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level(); // 获取世界级别
        BlockPos origin = context.origin(); // 生成原点
        RandomSource random = context.random(); // 随机数

        boolean placedAny = false; // 是否生成了云块

        for (int x = -8; x<=8; x++){ // x轴生成范围
            for (int z = -8; z<=8; z++){  // z轴生成范围
                // 使用圆形分布
                double distance = Math.sqrt(x * x + z * z); // 计算曼哈顿距离
                if (distance <= 8) {
                    double probability = 1.0 - (distance / 4.0) * 0.7; // 生成概率
                    // 厚度
                    for (int y = -1; y <= 1; y++) {
                        BlockPos pos = origin.offset(x, y, z); // 生成位置
                        
                        // 只在空气中生成，并且基于概率
                        if (level.isEmptyBlock(pos) && random.nextFloat() < probability) {
                            level.setBlock(pos, RegistryBlock.CLOUD_BLOCK.get().defaultBlockState(), 3); // 生成云块
                            placedAny = true; // 标记已生成
                        }
                    }
                }
            }
        }

        return placedAny;
    }

}
