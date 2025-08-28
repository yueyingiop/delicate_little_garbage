package com.core.DLG.enums;

import java.util.List;

import com.core.DLG.util.ColorHelper;

import net.minecraft.world.item.ArmorItem;

public enum TypeEnum {
    SWORD{
        @Override
        public String getString() { return "sword"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 0, 0)); }
    },
    SHOVEL{
        @Override
        public String getString() { return "shovel"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(139, 69, 19)); }
    },
    PICKAXE{
        @Override
        public String getString() { return "pickaxe"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(192, 192, 192)); }
    },
    AXE{
        @Override
        public String getString() { return "axe"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(0, 100, 0)); }
    },
    HOE{
        @Override
        public String getString() { return "hoe"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(210, 180, 140)); }
    },
    BOW{
        @Override
        public String getString() { return "bow"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(160, 82, 45)); }
    },
    CROSSBOW{
        @Override
        public String getString() { return "crossbow"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(139, 0, 0)); }
    },
    TRIDENT{
        @Override
        public String getString() { return "trident"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(0, 0, 255)); }
    },
    HELMET{
        @Override
        public String getString() { return ArmorItem.Type.HELMET.getName(); }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(220, 220, 220)); }
    },
    CHESTPLATE{
        @Override
        public String getString() { return ArmorItem.Type.CHESTPLATE.getName(); }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(169, 169, 169)); }
    },
    LEGGINGS{
        @Override
        public String getString() { return ArmorItem.Type.LEGGINGS.getName(); }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(128, 128, 128)); }
    },
    BOOTS{
        @Override
        public String getString() { return ArmorItem.Type.BOOTS.getName(); }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(105, 105, 105)); }
    },
    UNDEFINED_TYPE{
        @Override
        public String getString() { return "undefined_type"; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 255, 255)); }

    };
    public abstract String getString();
    public abstract int getColor();

    public static TypeEnum getType(String type){
        for(TypeEnum typeEnum : TypeEnum.values()){
            if(typeEnum.getString().equals(type)){
                return typeEnum;
            }
        }
        return TypeEnum.UNDEFINED_TYPE;
    }
}
