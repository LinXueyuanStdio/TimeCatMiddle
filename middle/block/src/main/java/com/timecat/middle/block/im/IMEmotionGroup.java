package com.timecat.middle.block.im;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-03
 * @description null
 * @usage null
 * 表情分组实体类
 */
public class IMEmotionGroup extends IMEmotionBean {

    // 表情组名字
    public String mName;

    /**
     * 表情集合
     */
    public List<IMEmotionItem> mEmotionItemList = new ArrayList<>();

    /**
     * 获取表情数量
     */
    public int getEmotionCount() {
        return mEmotionItemList.size();
    }
}
