package com.core.DLG.enums;

public enum DamageTypeEnum {
    CRITICAL(0, "critical", 0xFFFF55,0xFFAA00),
    PENETRATION(1, "penetration", 0x55FFFF,0x00AAAA),
    LIFESTEAL(2, "lifesteal", 0xFF5555,0xAA0000),
    HEALING(3, "healing",0x55FF55,0x00AA00),
    NORMAL(4, "normal", 0xFFFFFF,0xAAAAAA);


    private final int id;
    private final String name;
    private final int color;
    private final int colorOutline;

    DamageTypeEnum(int id, String name, int color, int colorOutline) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.colorOutline = colorOutline;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }

    public int getColorOutline() {
        return colorOutline;
    }

    public static DamageTypeEnum getById(int id) {
        for (DamageTypeEnum value : values()) {
            if (value.id == id) {
                return value;
            }
        }
        return null;
    }

    public static DamageTypeEnum getByName(String name) {
        for (DamageTypeEnum value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        return null;
    }

    
}
