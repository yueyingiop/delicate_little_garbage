package com.core.DLG.util.format;

import net.minecraft.ChatFormatting;

public enum FormatColor implements IColor {
    BLACK(ChatFormatting.BLACK),
    DARK_BLUE(ChatFormatting.DARK_BLUE),
    DARK_GREEN(ChatFormatting.DARK_GREEN),
    DARK_AQUA(ChatFormatting.DARK_AQUA),
    DARK_RED(ChatFormatting.DARK_RED),
    DARK_PURPLE(ChatFormatting.DARK_PURPLE),
    GOLD(ChatFormatting.GOLD),
    GRAY(ChatFormatting.GRAY),
    DARK_GRAY(ChatFormatting.DARK_GRAY),
    BLUE(ChatFormatting.BLUE),
    GREEN(ChatFormatting.GREEN),
    AQUA(ChatFormatting.AQUA),
    RED(ChatFormatting.RED),
    LIGHT_PURPLE(ChatFormatting.LIGHT_PURPLE),
    YELLOW(ChatFormatting.YELLOW),
    WHITE(ChatFormatting.WHITE);

    private final ChatFormatting formatting;

    public static FormatColor of(char index) {
        return FormatColor.of("0123456789abcdef".indexOf(Character.toLowerCase(index)));
    }

    public static FormatColor of(ChatFormatting formatting) {
        return FormatColor.of(formatting.getId());
    }

    public static FormatColor of(int index) {
        if (index >= 0 && index <= 15) {
            return FormatColor.values()[index];
        }
        return WHITE;
    }

    private FormatColor(ChatFormatting formatting) {
        if (!formatting.isColor()) {
            throw new IllegalArgumentException(formatting.getName());
        }
        this.formatting = formatting;
    }

    public ChatFormatting getFormatting() {
        return this.formatting;
    }

    @Override
    public int alpha() {
        return 255;
    }

    @Override
    public int red() {
        return this.getColorCode() >> 16 & 0xFF;
    }

    @Override
    public int green() {
        return this.getColorCode() >> 8 & 0xFF;
    }

    @Override
    public int blue() {
        return this.getColorCode() & 0xFF;
    }

    public int getColorCode() {
        char codeChar = "0123456789abcdef".charAt(this.formatting.getId());
        ChatFormatting formatting = ChatFormatting.getByCode(codeChar);

        if (formatting != null && formatting.isColor()) {
            Integer colorValue = formatting.getColor();
            if (colorValue != null) {
                return 0xFF000000 | colorValue; // 添加不透明Alpha通道
            }
        }
        return 0xFFFFFFFF; // 默认白色
    }

    @Override
    public int toInt() {
        return this.getColorCode();
    }
}
