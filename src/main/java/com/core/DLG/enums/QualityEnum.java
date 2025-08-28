package com.core.DLG.enums;

import java.util.List;

import com.core.DLG.util.ColorHelper;

public enum QualityEnum {
    QUALITY1{
        @Override
        public String getString() { return "quality1"; };

        @Override
        public int getNumber() { return 1; }

        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(180, 180, 180)); }
    },
    QUALITY2{
        @Override
        public String getString() { return "quality2"; }
        @Override
        public int getNumber() { return 2; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(0, 128, 0)); }
    },
    QUALITY3{
        @Override
        public String getString() { return "quality3"; }
        @Override
        public int getNumber() { return 3; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(70, 130, 180)); }
    },
    QUALITY4{
        @Override
        public String getString() { return "quality4"; }
        @Override
        public int getNumber() { return 4; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 215, 0)); }
    },
    QUALITY5{
        @Override
        public String getString() { return "quality5"; }
        @Override
        public int getNumber() { return 5; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 165, 0)); }
    },
    QUALITY6{
        @Override
        public String getString() { return "quality6"; }
        @Override
        public int getNumber() { return 6; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(160, 30, 220)); }
    },
    QUALITY7{
        @Override
        public String getString() { return "quality7"; }
        @Override
        public int getNumber() { return 7; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 50, 50)); }
    },
    QUALITY8{
        @Override
        public String getString() { return "quality8"; }
        @Override
        public int getNumber() { return 8; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 105, 180)); }
    },
    QUALITY9{
        @Override
        public String getString() { return "quality9"; }
        @Override
        public int getNumber() { return 9; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(0, 191, 255)); }
    },
    DEFAULT_QUALITY{
        @Override
        public String getString() { return "default_quality"; }
        @Override
        public int getNumber() { return 0; }
        @Override
        public int getColor() { return QUALITY3.getColor(); }
    },
    UNDEFINED_QUALITY{
        @Override
        public String getString() { return "undefined_quality"; }
        @Override
        public int getNumber() { return -1; }
        @Override
        public int getColor() { return ColorHelper.colorListParse(List.of(255, 255, 255)); }
    };

    public abstract String getString();
    public abstract int getNumber();
    public abstract int getColor();

    public static QualityEnum getQuality(String quality) {
        for (QualityEnum value : QualityEnum.values()) {
            if (value.getString().equals(quality)) {
                return value;
            }
        }
        return UNDEFINED_QUALITY;
    }

    public static QualityEnum getQuality(int quality) {
        for (QualityEnum value : QualityEnum.values()) {
            if (value.getNumber() == quality) {
                return value;
            }
        }
        return UNDEFINED_QUALITY;
    }
}
