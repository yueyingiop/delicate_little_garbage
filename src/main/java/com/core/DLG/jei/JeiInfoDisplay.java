package com.core.DLG.jei;

import java.util.ArrayList;
import java.util.List;

import com.core.DLG.DLG;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public class JeiInfoDisplay {
    public String itemId;
    public Component buildDescription;

    public JeiInfoDisplay(ItemStack itemStack, List<String> detailsTitle) {
        this(itemStack, detailsTitle, createEmptyTitleStyle(detailsTitle.size()), createEmptyStyleList(detailsTitle.size()));
    }

    public JeiInfoDisplay(ItemStack itemStack, List<String> detailsTitle, List<Style> titleStyle) {
        this(itemStack, detailsTitle, titleStyle, createEmptyStyleList(detailsTitle.size()));
    }

    private static List<Style> createEmptyTitleStyle(int size) {
        List<Style> emptyStyleList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            emptyStyleList.add(Style.EMPTY);
        }
        return emptyStyleList;
    }
    private static List<List<Style>> createEmptyStyleList(int size) {
        List<List<Style>> styleList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            styleList.add(List.of());
        }
        return styleList;
    }

    public JeiInfoDisplay(ItemStack itemStack, List<String> detailsTitle, List<Style> titleStyle, List<List<Style>> styleList) {
        this.itemId = ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getPath();
        this.buildDescription = buildDescription(
            setInfo(
                createInfoData(
                    true,
                    createInfoGroupList(
                        detailsTitle, 
                        titleStyle, 
                        styleList
                    )
                )
            )
        );
    }

    public Component buildDescription() {
        return this.buildDescription;
    }

    public static List<Component> setInfo(InfoData infoData) {
        List<Component> info = new ArrayList<>();

        String itemId = infoData.getItemId();
        List<InfoGroup> infoGroupList = infoData.getInfoGroup();

        if (infoData.getTitle()) {
            info.add(
                Component.translatable(
                    "jei." + DLG.MODID + ".info.title",
                    Component.translatable("item." + DLG.MODID + "." + itemId)
                )
            );
        }

        for (InfoGroup infoGroup : infoGroupList) { 
            // 添加空行
            info.add(Component.literal(""));

            if (infoGroup.getTitle()) { 
                info.add(
                    Component.translatable("jei." + DLG.MODID + ".info." + itemId + "." + infoGroup.getDetailsTitle() + ".title")
                        .withStyle(infoGroup.getTitleStyle())
                );
            }
            for (Component infoComponent : infoGroup.getInfo()) { 
                info.add(infoComponent); 
            }
        }
        return info;
    }

    public static Component buildDescription(InfoData infoData) {
        return buildDescription(setInfo(infoData));
    }

    public static Component buildDescription(List<Component> components) {
        if (components.isEmpty()) {
            return Component.empty();
        }
        Component result = components.get(0);
        for (int i = 1; i < components.size(); i++) {
            result = result.copy().append(Component.literal("\n")).append(components.get(i));
        }
        return result;
    }

    public InfoData createInfoData(Boolean title, List<InfoGroup> infoGroupList) {
        return new InfoData(this.itemId, title, infoGroupList);
    }

    public List<InfoGroup> createInfoGroupList(List<String> detailsTitle, List<Style> titleStyle, List<List<Style>> styleList) {
        List<InfoGroup> infoGroupList = new ArrayList<>();
        for (int i = 0; i < detailsTitle.size(); i++) {
            InfoGroup infoGroup = new InfoGroup(
                this.itemId, 
                detailsTitle.get(i), 
                titleStyle.get(i)
            );
            infoGroup.addInfo(styleList.get(i));
            infoGroupList.add(infoGroup);
        }
        return infoGroupList;
    }


    /**
     * 信息数据
    */
    public static class InfoData {
        private String itemId;
        private Boolean title;
        private List<InfoGroup> infoGroupList;

        public InfoData(ItemStack stack, Boolean title, List<InfoGroup> infoGroupList) {
            this(
                ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath(), 
                title, infoGroupList
            );
        }

        public InfoData(String itemId, Boolean title, List<InfoGroup> infoGroupList) {
            this.itemId = itemId;
            this.title = title;
            this.infoGroupList = infoGroupList;
        }

        public String getItemId() {
            return itemId;
        }

        public Boolean getTitle() {
            return title;
        }

        public List<InfoGroup> getInfoGroup() {
            return infoGroupList;
        }

        public InfoData getThis() {
            return this;
        }
    }

    /**
     * 信息组
    */
    public static class InfoGroup {
        private String itemId;
        private Boolean title; // 是否使用标题
        private String detailsTitle; // 详情标题
        private Style titleStyle; // 标题样式
        private List<Component> info; // 详情信息
        private List<Style> styleList; // 信息样式

        // 构造函数
        public InfoGroup(String itemId) {
            this(itemId, null, Style.EMPTY);
            this.title = false;
        }

        // 构造函数
        public InfoGroup(ItemStack stack) {
            this(stack, null, Style.EMPTY);
            this.title = false;
        }

        // 构造函数
        public InfoGroup(ItemStack stack, String detailsTitle, Style titleStyle) {
            this(ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath(), detailsTitle, titleStyle);
        }

        // 构造函数
        public InfoGroup(String itemId, String detailsTitle, Style titleStyle) {
            this.itemId = itemId;
            this.title = true;
            this.detailsTitle = detailsTitle;
            this.titleStyle = titleStyle;
            this.info = new ArrayList<>();
            this.styleList = new ArrayList<>();
        }

        public InfoGroup addInfo() {
            this.addInfo(new ArrayList<>());
            return this;
        }
        // 添加一跳信息
        public InfoGroup addInfo(List<Style> styleList) {
            int line = 0;
            while (true) {
                String key = "jei." + DLG.MODID + ".info." + itemId + "." + detailsTitle + ".line" + line;
                MutableComponent component = Component.translatable(key); // 获取多语言文本
            
                if (component.getString().equals(key)) break; // 如果没有更多行，退出循环
                
                Style style = line < styleList.size() ? styleList.get(line) : Style.EMPTY; // 获取样式
                this.info.add(component.withStyle(style));
                
                line++;
            }
            return this;
        }

        // 获取标题
        public Boolean getTitle() {
            return title;
        }

        // 获取详情标题
        public String getDetailsTitle() {
            return detailsTitle;
        }

        // 获取标题样式
        public Style getTitleStyle() {
            return titleStyle;
        }

        // 获取详情信息
        public List<Component> getInfo() {
            return info;
        }

        // 获取样式详情
        public List<Style> getStyleList() {
            return styleList;
        }
    }
}
