<div align="center">

[English](README.md) | [中文](README_CN.md)

</div>

# DLG{#Ttop}
<hr/>

## 说明
该mod修改了一些原版的机制,例如:
- 自定义堆叠
- 取消物品冷却
- 修改饥饿值上限
- 取消附魔冲突
- 是否可以白天睡觉
- ...

另外添加了了一些物品(方块/生物),例如:
- 装备碎片
- 不毁卷轴
- 云朵方块
- 云鲸
- ...

目前已经适配了苹果皮和Jei
相关配置请查看(/config/DLG/)路径下的json文件

#### 如果你有其他想法请提交议题(Issues)
<hr/>

## 配置文件
<details>
    <summary style="font-size:18px">item-config.json</summary>
    <table>
        <thead>
            <tr>
                <th>配置项</th>
                <th>说明</th>
                <th>默认值</th>
                <th>取值范围</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>maxStackSize</td>
                <td>设置物品最大堆叠</td>
                <td align="center">64</td>
                <td align="center">1 ~ 2147483646</td>
                <td align="center">包括1堆叠物品(例如:钻石剑,潜影盒等)</td>
            </tr>
            <tr>
                <td>cancelItemCooldowns</td>
                <td>是否取消物品冷却</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>equipmentDebris  装备碎片相关配置</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td>itemBrokenDrops</td>
                                    <td>物品损坏后是否掉落碎片</td>
                                    <td align="center">true</td>
                                    <td align="center">true 或 false</td>
                                    <td align="center">
                                        依据equipmentDebris.configList列表里的配置决定什么物品掉落什么品质和类型的碎片
                                    </td>
                                </tr>
                                <tr>
                                    <td>alwaysDrops</td>
                                    <td>物品损坏后是否总是掉落碎片</td>
                                    <td align="center">true</td>
                                    <td align="center">true 或 false</td>
                                    <td align="center">
                                        依据equipmentDebris.alwaysList列表决定,没有在equipmentDebris.configList里配置的物品是否掉落碎片
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>configList  装备碎片配置列表</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>item</td>
                                                    <td>物品ID或标签</td>
                                                </tr>
                                                <tr>
                                                    <td>type</td>
                                                    <td>物品损坏后掉落的碎片类型</td>
                                                </tr>
                                                <tr>
                                                    <td>quality</td>
                                                    <td>物品损坏后掉落的碎片品质</td>
                                                </tr>
                                            </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>alwaysList  装备碎片配置列表</summary>
                                            <div>
                                                未在equipmentDebris.configList列表中的物品,但是包含该标签掉落默认的碎片
                                            </div>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>upgradeMaterials  装备碎片升级材料列表</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>quality</td>
                                                    <td>碎片品质</td>
                                                </tr>
                                                <tr>
                                                    <td>upgradeMaterial</td>
                                                    <td>装备碎片升级所需的材料</td>
                                                </tr>
                                            </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>upgradeMaterials  经验材料列表</summary>
                                            <div>
                                                该配置只能在碎片锻造台中为绑定了碎片的物品提供经验
                                            </div>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>expMaterial</td>
                                                    <td>可以提供经验的材料</td>
                                                </tr>
                                                <tr>
                                                    <td>exp</td>
                                                    <td>材料所提供的经验</td>
                                                </tr>
                                            </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </details>
                </td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>C&C  双爆相关配置</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td>customAttribute</td>
                                    <td>是否开启自定义属性</td>
                                    <td align="center">true</td>
                                    <td align="center">true 或 false</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td>playerDefaultCriticalChance</td>
                                    <td>玩家默认暴击率</td>
                                    <td align="center">0.05</td>
                                    <td align="center">0 ~ 1</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td>playerDefaultCriticalDamage</td>
                                    <td>玩家默认暴击伤害</td>
                                    <td align="center">0.5</td>
                                    <td align="center">0 ~ 512</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>C&CItemList  物品双爆列表</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>item</td>
                                                        <td>需要添加双爆的物品</td>
                                                    </tr>
                                                    <tr>
                                                        <td>criticalChance</td>
                                                        <td>暴击率</td>
                                                    </tr>
                                                    <tr>
                                                        <td>criticalDamage</td>
                                                        <td>暴击伤害</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </details>
                </td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>customAttribute  属性相关配置</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>attributeType  属性类型配置</summary>
                                            <table>
                                                <tbody> 
                                                    <tr>
                                                        <td>customType</td>
                                                        <td>通用类型</td>
                                                    </tr>
                                                    <tr>
                                                        <td>attackType</td>
                                                        <td>攻击类型</td>
                                                    </tr>
                                                    <tr>
                                                        <td>defenseType</td>
                                                        <td>防御类型</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>attribute</td>
                                                        <td>属性修饰符</td>
                                                    </tr>
                                                    <tr>
                                                        <td>baseValue</td>
                                                        <td>基础值</td>
                                                    </tr>
                                                    <tr>
                                                        <td>baseRange</td>
                                                        <td>基础值偏移范围</td>
                                                    </tr>
                                                    <tr>
                                                        <td>updateValue</td>
                                                        <td>升级提供的额外数值</td>
                                                    </tr>
                                                    <tr>
                                                        <td>updateRange</td>
                                                        <td>额外值偏移范围</td>
                                                    </tr>
                                                    <tr>
                                                        <td>operation</td>
                                                        <td>计算方法</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>entrySetting  词条设定</summary>
                                            <div>
                                                声明了说明品质的装备碎片具有多少词条
                                            </div>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>参数</th>
                                                        <th>说明</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>quality</td>
                                                        <td>碎片品质</td>
                                                    </tr>
                                                    <tr>
                                                        <td>basicEntriesCount</td>
                                                        <td>基础词条数量</td>
                                                    </tr>
                                                    <tr>
                                                        <td>maxEntriesCount</td>
                                                        <td>最大词条数量</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>                                
                            </tbody>
                        </table>
                    </details>
                </td>
            </tr>
        </tbody>
    </table>
</details>

<details>
    <summary style="font-size:18px">player-config.json</summary>
    <table>
        <thead>
            <tr>
                <th>配置项</th>
                <th>说明</th>
                <th>默认值</th>
                <th>取值范围</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>cancelAttackGap</td>
                <td>是否取攻击间隔</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">只取消了剑和斧子的攻击间隔</td>
            </tr>
            <tr>
                <td>cancelInvulnerableDuration</td>
                <td>是否取消无敌时间</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">只取消了敌对生物的无敌事件</td>
            </tr>
            <tr>
                <td>alwaysEat</td>
                <td>是否可以一直食用食物</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td>alwaysSleep</td>
                <td>是否可以在任意时间睡觉</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td>sleepDurationTime</td>
                <td>睡觉所跳过的时间</td>
                <td align="center">12000</td>
                <td align="center">0 ~ 24000</td>
                <td align="center">只有 alwaysSleep 为true是生效</td>
            </tr>
            <tr>
                <td>sleepEverywhere</td>
                <td>是否可以在任意维度睡觉</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>foodData  饥饿值相关配置</summary>
                        <table> 
                            <tbody> 
                                <tr>
                                    <td>maxHungry</td>
                                    <td>最大饥饿值为多少</td>
                                    <td align="center">40</td>
                                    <td align="center">0 ~ 2147483646</td>
                                    <td align="center">已经适配了苹果皮</td>
                                </tr>
                                <tr>
                                    <td>minHealFoodLevel</td>
                                    <td>回复最小值</td>
                                    <td align="center">20</td>
                                    <td align="center">0 ~ maxHungry</td>
                                    <td align="center">
                                        当前饥饿值大于回复最小值且饱和度为0时,会消耗饥饿值回复生命
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </details>
                </td>
            </tr>
            <tr>
                <td>damageHUD</td>
                <td>统计伤害总量</td>
                <td align="center">false</td>
                <td align="center">true 或 false</td>
                <td align="center">开启后会在屏幕右上角显示一段时间内的总伤害</td>
            </tr>
        </tbody>
    </table>
</details>

<details>
    <summary style="font-size:18px">enchantment-config.json</summary>
    <table>
        <thead>
            <tr>
                <th>配置项</th>
                <th>说明</th>
                <th>默认值</th>
                <th>取值范围</th>
                <th>备注</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>trulyInfinite</td>
                <td>是否开启真无限</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">开启后背包内即使没有箭也可以装填(必须附魔无限才生效)</td>
            </tr>
            <tr>
                <td>infinityCrossbow</td>
                <td>是否令弩也可以附魔无限</td>
                <td align="center">true</td>
                <td align="center">true 或 false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                    <summary>cancelEnchantmentCombat  附魔冲突相关配置</summary>
                    <table> 
                        <tbody> 
                            <tr>
                                <td>attack</td>
                                <td>是否取消攻击类附魔冲突</td>
                                <td align="center">true</td>
                                <td align="center">true 或 false</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>protection</td>
                                <td>是否取消防御类附魔冲突</td>
                                <td align="center">true</td>
                                <td align="center">true 或 false</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>infiniteAndMending</td>
                                <td>是否取消无限与经验修补附魔冲突</td>
                                <td align="center">true</td>
                                <td align="center">true 或 false</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>pierceAndMutiShooting</td>
                                <td>是否取消穿透与多重射击附魔冲突</td>
                                <td align="center">true</td>
                                <td align="center">true 或 false</td>
                                <td align="center">-</td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>
</details> 

<hr/>

## 属性修饰符

<table>
    <thead>
        <tr>
            <th>属性修饰符</th>
            <th>说明</th>
            <th>默认值</th>
            <th>取值范围</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>attribute.dlg.critical_chance</td>
            <td>暴击率</td>
            <td align="center">0.05</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.critical_damage</td>
            <td>暴击伤害</td>
            <td align="center">0.5</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.dodge</td>
            <td>闪避率</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.penetration_chance</td>
            <td>穿透率</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.penetration_damage</td>
            <td>穿透伤害</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.lifesteal_chance</td>
            <td>吸血率</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.lifesteal_damage</td>
            <td>吸血伤害</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.hp_regen</td>
            <td>生命恢复</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.healing_bonus</td>
            <td>治疗加成</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
    </tbody>
</table>

<hr/>

## 修改/新增机制

<table>
    <thead>
        <tr>
            <th>机制</th>
            <th>修改/新增</th>
            <th>说明</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>物品堆叠</td>
            <td>修改</td>
            <td>现在游戏中所有的物品都可以进行堆叠并自定义堆叠数量,包括药水、装备、潜影盒... ...</td>
        </tr>
        <tr>
            <td>物品冷却</td>
            <td>修改</td>
            <td>取消了原版物品的使用冷却,例如投掷末影珍珠</td>
        </tr>
        <tr>
            <td>食物</td>
            <td>修改</td>
            <td>现在你可以一直使用物品,不再受饥饿值限制</td>
        </tr>
        <tr>
            <td>饥饿值</td>
            <td>修改</td>
            <td>现在你可以修改最大饥饿值(包含饱食度),已适配苹果皮mod</td>
        </tr>
        <tr>
            <td>床</td>
            <td>修改</td>
            <td>现在你可以在任意时间睡觉,并且可以在任何地方睡觉</td>
        </tr>
        <tr>
            <td>攻击间隔</td>
            <td>修改</td>
            <td>现在取消了剑和斧子的攻击间隔,你可以一直攻击</td>
        </tr>
        <tr>
            <td>无敌时间</td>
            <td>修改</td>
            <td>现在取消了敌对生物的无敌时间,只要你点的够快,你就可以一招秒杀生物</td>
        </tr>
        <tr>
            <td>附魔</td>
            <td>修改</td>
            <td>现在取消了部分附魔的冲突,现在可以实现既要有要</td>
        </tr>
        <tr>
            <td>无限</td>
            <td>修改</td>
            <td>现在你的背包中即使没有箭也可以使用弓或弩</td>
        </tr>
        <tr>
            <td>字体</td>
            <td>修改</td>
            <td>现在你可以使用HEX为物品重命名来达到改变颜色的目的,例如#FF0000木剑</td>
        </tr>
        <tr>
            <td>装备碎片</td>
            <td>新增</td>
            <td>现在部分物品虽坏你可以获取装备碎片,具体用处可以在游戏内查看(添加JEI后才可以查看)</td>
        </tr>
        <tr>
            <td>猫薄荷</td>
            <td>新增</td>
            <td>现在添加了一个新的作物和相关物品,他会修改猫和豹猫的行为逻辑,使他们不再逃跑</td>
        </tr>
        <tr>
            <td>可合成无法破坏</td>
            <td>新增</td>
            <td>现在添加了一个新的物品,它可以为任意物品添加无法破坏</td>
        </tr>
    </tbody>
</table>

<hr/>

## 其他介绍(物品,方块,生物... ...)
适配了jei 可以通过安装jei查看物品说明