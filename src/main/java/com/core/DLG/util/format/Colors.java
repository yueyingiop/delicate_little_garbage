package com.core.DLG.util.format;

import net.minecraft.ChatFormatting;

public class Colors {
    public static IColor of(int index) {
        return FormatColor.of(index);
    }

    public static IColor of(char index) {
        return FormatColor.of(index);
    }

    public static IColor of(ChatFormatting formatting) {
        return FormatColor.of(formatting);
    }

    public static IColor of(String s) {
        return SimpleColor.of(s);
    }
}
