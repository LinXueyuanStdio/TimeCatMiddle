package com.timecat.middle.block.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.timecat.middle.block.R;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-26
 * @description null
 * @usage null
 */
public class MiniBookNavView extends LinearLayout {
    public ImageView left;
    public ImageView index;
    public ImageView apps;
    public ImageView chat;
    public ImageView right;
    Listener listener;

    public MiniBookNavView(Context context) {
        super(context);
        init(context);
    }

    public MiniBookNavView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MiniBookNavView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MiniBookNavView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        inflate(context, R.layout.view_book_navi_mini, this);

        left = findViewById(R.id.left);
        index = findViewById(R.id.index);
        apps = findViewById(R.id.apps);
        chat = findViewById(R.id.chat);
        right = findViewById(R.id.right);

        setListeners();
    }

    public void setListeners() {
        left.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickLeft(v);
            }
        });
        index.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickIndex(v);
            }
        });
        apps.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickApps(v);
            }
        });
        chat.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickChat(v);
            }
        });
        right.setOnClickListener(v -> {
            if (listener != null) {
                listener.clickRight(v);
            }
        });
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void clickLeft(View view);

        void clickIndex(View view);

        void clickApps(View view);

        void clickChat(View view);

        void clickRight(View view);
    }
}
