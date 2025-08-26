package com.core.DLG.enums;

public enum TypeEnum {
    SWORD{
        @Override
        public String getString() { return "sword"; }
    },
    SHOVEL{
        @Override
        public String getString() { return "shobel"; }
    },
    PICKAXE{
        @Override
        public String getString() { return "pickaxe"; }
    },
    AXE{
        @Override
        public String getString() { return "axe"; }
    },
    HOE{
        @Override
        public String getString() { return "hoe"; }
    },
    BOW{
        @Override
        public String getString() { return "bow"; }
    },
    CROSSBOW{
        @Override
        public String getString() { return "crossbow"; }
    },
    TRIDENT{
        @Override
        public String getString() { return "trident"; }
    },
    UNDEFINED_TYPE{
        @Override
        public String getString() { return "undefined_type"; }

    };
    public abstract String getString();
    public static TypeEnum getType(String type){
        for(TypeEnum typeEnum : TypeEnum.values()){
            if(typeEnum.getString().equals(type)){
                return typeEnum;
            }
        }
        return TypeEnum.UNDEFINED_TYPE;
    }
}
