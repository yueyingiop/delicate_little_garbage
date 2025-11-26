<div align="center">

[English](README.md) | [中文](README_ZH.md)

</div>

# DLG{#Ttop}
<hr/>

## Explaination
This mod modifies some rules of vanilla Minecraft,such as:
- customize maxStackSize
- remove item's cooldown
- modify max hunger value
- remove enchantment comflict
- be able to sleep all the time
- ...

Addition,it also adds some items(blocks or entities),such as:
- Equipment debris(item)
- Unbreakable scroll(item)
- cloud block(block)
- cloud whale(block)
- ...

Now,it has been compatible to `Just Enough Items` and `AppleSkin`
If you want to modify the relevant config ,please check the `json` files under the path of `../config/DLG/`


#### Meanwhile,if you have any ideas of this mod please push `Issues` to my Github
<hr/>

## Config Details
<details>
    <summary style="font-size:18px">item-config.json</summary>
    <table>
        <thead>
            <tr>
                <th>Config</th>
                <th>Explaination</th>
                <th>Default Value</th>
                <th>Acceptable Range</th>
                <th>Comment</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>maxStackSize</td>
                <td>Set itemstack maxStackSize</td>
                <td align="center">64</td>
                <td align="center">1 ~ 2147483646</td>
                <td align="center">Also contain the unstackable items(Such as Diamond Sword,Shulker Box .etc)</td>
            </tr>
            <tr>
                <td>cancelItemCooldowns</td>
                <td>Remove the item's cooldown</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>equipmentDebris config</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td>itemBrokenDrops</td>
                                    <td>Drop debris after item broken</td>
                                    <td align="center">true</td>
                                    <td align="center">true or false</td>
                                    <td align="center">
                                        According to the config in the `equipmentDebris.configList` list,it will decide which rarity and type debris should drop.
                                    </td>
                                </tr>
                                <tr>
                                    <td>alwaysDrops</td>
                                    <td>Always drop debris after item broken</td>
                                    <td align="center">true</td>
                                    <td align="center">true or false</td>
                                    <td align="center">
                                        When the item in `equipmentDebris.alwaysList` breaks,it will always drop debris.Whether in `equipmentDebris.configLis` or not.
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>configList EquipmentDebrisConfigList</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>item</td>
                                                    <td>item's ID or tag</td>
                                                </tr>
                                                <tr>
                                                    <td>type</td>
                                                    <td>which type of debris should drop when item broke</td>
                                                </tr>
                                                <tr>
                                                    <td>quality</td>
                                                    <td>which rarity of debris should drop when item broke</td>
                                                </tr>
                                            </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>alwaysList EquipmentDebrisConfigList</summary>
                                            <div>
                                                When the item which is in `equipmentDebris.alwaysList` and not in `equipmentDebris.configList` broke,it will drop the default debris(Called 'Unknown Debris')
                                            </div>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>upgradeMaterials  EquipmentDebrisUpgradeMaterialList</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>quality</td>
                                                    <td>The rarity of debris</td>
                                                </tr>
                                                <tr>
                                                    <td>upgradeMaterial</td>
                                                    <td>The material which will be cost when the equipment debris upgrades</td>
                                                </tr>
                                            </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>upgradeMaterials  EXPMaterialList</summary>
                                            <div>
                                                This configured EXPMaterial could only provide EXP to debris_bound item in the debris smithing_table  
                                            </div>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                <tr>
                                                    <td>expMaterial</td>
                                                    <td>The material which could provide EXP</td>
                                                </tr>
                                                <tr>
                                                    <td>exp</td>
                                                    <td>The EXP which the EXPMaterial will be provided</td>
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
                        <summary>Critical rate & damage(C&C) Config</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td>customAttribute</td>
                                    <td>enable or diaable custom attribute</td>
                                    <td align="center">true</td>
                                    <td align="center">true or false</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td>playerDefaultCriticalChance</td>
                                    <td>player's default Critical Rate</td>
                                    <td align="center">0.05</td>
                                    <td align="center">0 ~ 1</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td>playerDefaultCriticalDamage</td>
                                    <td>player's default Critical Damage</td>
                                    <td align="center">0.5</td>
                                    <td align="center">0 ~ 512</td>
                                    <td align="center"> - </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>C&CItemList</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>item</td>
                                                        <td>Which item is needed to add C&C</td>
                                                    </tr>
                                                    <tr>
                                                        <td>criticalChance</td>
                                                        <td>Critical Rate</td>
                                                    </tr>
                                                    <tr>
                                                        <td>criticalDamage</td>
                                                        <td>Critical Damage</td>
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
                        <summary>customAttribute</summary>
                        <table> 
                            <tbody>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>details</summary>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>attribute</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td>baseValue</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td>baseRange</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td>updateValue</td>
                                                        <td>the Extra Value which upgrade provided</td>
                                                    </tr>
                                                    <tr>
                                                        <td>updateRange</td>
                                                        <td>the extra range which upgrade provided</td>
                                                    </tr>
                                                    <tr>
                                                        <td>operation</td>
                                                        <td>-</td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </details>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan=5>
                                        <details>
                                            <summary>entrySetting</summary>
                                            <div>
                                                The settings declared how many entries the equipments debris of different rarity could contain
                                            </div>
                                            <table>
                                                <thead>
                                                    <tr>
                                                        <th>Arguments</th>
                                                        <th>Explaination</th>
                                                    </tr>
                                                </thead>
                                                <tbody> 
                                                    <tr>
                                                        <td>quality</td>
                                                        <td>The debris's rarity</td>
                                                    </tr>
                                                    <tr>
                                                        <td>basicEntriesCount</td>
                                                        <td>-</td>
                                                    </tr>
                                                    <tr>
                                                        <td>maxEntriesCount</td>
                                                        <td>-</td>
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
                <th>Config</th>
                <th>Explaination</th>
                <th>Default Value</th>
                <th>Valid Range</th>
                <th>Comments</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>cancelAttackGap</td>
                <td>enable or disable to cancel attack gap</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">Only for Swords and Axes</td>
            </tr>
            <tr>
                <td>cancelInvulnerableDuration</td>
                <td>enable or disable to cancel Invulnerable Duration</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">Only for hostile entity</td>
            </tr>
            <tr>
                <td>alwaysEat</td>
                <td>enable or disable to eat whenever you want</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td>alwaysSleep</td>
                <td>enable or disable to sleep whenever you want</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td>sleepDurationTime</td>
                <td>How many ticks the player's sleep will skip </td>
                <td align="center">12000</td>
                <td align="center">0 ~ 24000</td>
                <td align="center">Only when 'alwaysSleep' is true will this be available</td>
            </tr>
            <tr>
                <td>sleepEverywhere</td>
                <td>enable or disable to sleep in any dimension</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                        <summary>foodData  hunger value config</summary>
                        <table> 
                            <tbody> 
                                <tr>
                                    <td>maxHungry</td>
                                    <td>-</td>
                                    <td align="center">40</td>
                                    <td align="center">0 ~ 2147483646</td>
                                    <td align="center">it has been compatible to `AppleSkin`</td>
                                </tr>
                                <tr>
                                    <td>minHealFoodLevel</td>
                                    <td>the min hunger value which player should have to regenerate </td>
                                    <td align="center">20</td>
                                    <td align="center">0 ~ maxHungry</td>
                                    <td align="center">
                                        When current hunger value is more than minHealFoodLevel and saturation value is equal to 0 , players will cost hunger value to heal themselves
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </details>
                </td>
            </tr>
            <tr>
                <td>damageHUD</td>
                <td>calculate total damage</td>
                <td align="center">false</td>
                <td align="center">true or false</td>
                <td align="center">When it's enable,it will show total damage for a period of time at top right corner of screen</td>
            </tr>
        </tbody>
    </table>
</details>

<details>
    <summary style="font-size:18px">enchantment-config.json</summary>
    <table>
        <thead>
            <tr>
                <th>Config</th>
                <th>Explaination</th>
                <th>Default Value</th>
                <th>Valid Range</th>
                <th>Comments</th>
            </tr>
        </thead>
        <tbody> 
            <tr>
                <td>trulyInfinite</td>
                <td>enable or disable 'true' infinite</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">Bow could still shoot even if there is no arrow in your inventory(must enchant the bow with infinite)</td>
            </tr>
            <tr>
                <td>infinityCrossbow</td>
                <td>make crossbow could be enchanted with infinite</td>
                <td align="center">true</td>
                <td align="center">true or false</td>
                <td align="center">-</td>
            </tr>
            <tr>
                <td colspan=5>
                    <details>
                    <summary>cancelEnchantmentCombat  enchantmentCombatConfig</summary>
                    <table> 
                        <tbody> 
                            <tr>
                                <td>attack</td>
                                <td>cancel attack enchantment combat</td>
                                <td align="center">true</td>
                                <td align="center">true or false</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>protection</td>
                                <td>cancel protection enchantment combat</td>
                                <td align="center">true</td>
                                <td align="center">true or false</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>infiniteAndMending</td>
                                <td>cancel infinite and mending combat</td>
                                <td align="center">12000</td>
                                <td align="center">0 ~ 24000</td>
                                <td align="center">-</td>
                            </tr>
                            <tr>
                                <td>pierceAndMutiShooting</td>
                                <td>cancel pierce and MutiShooting combat</td>
                                <td align="center">true</td>
                                <td align="center">true or false</td>
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

## Attribute Modification Key

<table>
    <thead>
        <tr>
            <th>Attribute Modification Key</th>
            <th>Explaination</th>
            <th>Default Value</th>
            <th>Valid Range</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>attribute.dlg.critical_chance</td>
            <td>Critical Rate</td>
            <td align="center">0.05</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.critical_damage</td>
            <td>Critical Damage</td>
            <td align="center">0.5</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.dodge</td>
            <td>Dodge Chance</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.penetration_chance</td>
            <td>Penetration Chance</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.penetration_damage</td>
            <td>Penetration Damage</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.lifesteal_chance</td>
            <td>Life_steal Rate </td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 1.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.lifesteal_damage</td>
            <td>Life_steal Damage</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.hp_regen</td>
            <td>Regeneration</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
        <tr>
            <td>attribute.dlg.healing_bonus</td>
            <td>Heal Boost</td>
            <td align="center">0.0</td>
            <td align="center">0.0 ~ 512.0</td>
        </tr>
    </tbody>
</table>

<hr/>

## Modification/Addition

<table>
    <thead>
        <tr>
            <th>Rule</th>
            <th>Modification/Addition</th>
            <th>Explaination</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>MaxItemStackSize</td>
            <td>Modification</td>
            <td>Now you can set the max stack size of any item in Minecraft.Potions,equipments and any other unstackable item are also contained</td>
        </tr>
        <tr>
            <td>ItemCooldown</td>
            <td>Modification</td>
            <td>cancel item's cooldown in vanilla Minecraft.Such as ender_pearl </td>
        </tr>
        <tr>
            <td>Food</td>
            <td>Modification</td>
            <td>Now you can eat whenever you want.</td>
        </tr>
        <tr>
            <td>HungerValue</td>
            <td>Modification</td>
            <td>Now you can modify the max hunger value(also contain max saturation value).It has been compatiable to `AppleSkin` mod</td>
        </tr>
        <tr>
            <td>Bed</td>
            <td>Modification</td>
            <td>Now you could sleep whenever and wherever you want</td>
        </tr>
        <tr>
            <td>Attack Gap</td>
            <td>Modification</td>
            <td>Now it has cancelled the attack gap of swords and axes.That means you can always attack</td>
        </tr>
        <tr>
            <td>InvulnerableTick</td>
            <td>Modification</td>
            <td>Now we have cancelled the invulnerable tick of hostile mobs.That means you can kill any mob immediately if you can attack quickly enough</td>
        </tr>
        <tr>
            <td>EnchantMents</td>
            <td>Modification</td>
            <td>Now we have cancelled some combat between certain enchantments.That means you can enchant all of them on your equipments </td>
        </tr>
        <tr>
            <td>Infinite</td>
            <td>Modification</td>
            <td>Now you can use your bow or crossbow to shoot although there is no arrow in your inventory</td>
        </tr>
        <tr>
            <td>Font</td>
            <td>Modification</td>
            <td>Now you can use HEX to rename the item for changing its displayname color,Such as '#FF0000Wooden sword'</td>
        </tr>
        <tr>
            <td>Equipment Debris</td>
            <td>Addition</td>
            <td>Now you can get equipment debris when certain item has broken.You can check Jei in game for details</td>
        </tr>
        <tr>
            <td>Nepeta cataria</td>
            <td>Addition</td>
            <td>Now we have added a new kind of grain and its relevant item.These items will modify Ai of cat and Ocelot,they will not run away from you when you take these items</td>
        </tr>
        <tr>
            <td>Unbreakable</td>
            <td>Addition</td>
            <td>Now we have added a new item which can add 'Unbreakable' to any item</td>
        </tr>
    </tbody>
</table>

<hr/>

## Others
This mod is compatible to `jei`,you can use `jei` to check the explaination of each item above