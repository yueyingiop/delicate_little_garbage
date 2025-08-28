package com.core.DLG.util.format;

import net.minecraft.ChatFormatting;

public class Utils {
    public static ChatFormatting formattingOf(char c) {
        int index = "0123456789abcdefklmnor".indexOf(Character.toLowerCase(c));
        if (index < 0) {
            return null;
        }
        return ChatFormatting.values()[index];
    }
}
