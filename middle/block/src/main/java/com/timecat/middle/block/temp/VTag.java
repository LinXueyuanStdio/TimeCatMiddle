package com.timecat.middle.block.temp;

import com.timecat.data.system.model.entity.ITag;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-25
 * @description null
 * @usage null
 */
public class VTag implements ITag {
    String name;
    int color;

    public VTag(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
