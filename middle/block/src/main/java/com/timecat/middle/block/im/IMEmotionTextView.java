package com.timecat.middle.block.im;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-03
 * @description null
 * @usage null
 */

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 2019/05/30 09:39
 *
 * 可以处理表情的 TextView
 */
public class IMEmotionTextView extends AppCompatTextView {

    private boolean isEnableEmotion = true;
    private List<IMEmotionGroup> mEmotionGroupList = new ArrayList<>();


    public IMEmotionTextView(Context context) {
        this(context, null);
    }

    public IMEmotionTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IMEmotionTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写，重新实现文本的表情符处理
     */
    @Override
    public void setText(CharSequence text, BufferType type) {
        Spannable spannable = handleEmotion(text);
        super.setText(spannable, type);
    }

    /**
     * 设置是否启用 TextView 识别表情功能
     */
    public void setEnableEmotion(boolean enable) {
        isEnableEmotion = enable;
    }

    /**
     * 处理表情
     *
     * @param text 需要处理的文本内容
     */
    protected Spannable handleEmotion(CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        if (!isEnableEmotion) {
            return new SpannableString(text);
        }
        return IMEmotionManager.getInstance().getEmotionSpannable(text);
    }
}

