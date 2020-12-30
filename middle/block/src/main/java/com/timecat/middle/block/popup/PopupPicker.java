package com.timecat.middle.block.popup;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.RecyclerView;

import com.timecat.middle.block.R;
import com.timecat.middle.block.util.DisplayUtil;

/**
 * Created by ywwynm on 2015/8/18.
 * Simple Picker for EverythingDone using a PopupWindow to show contents.
 */
public abstract class PopupPicker {

    public static String TAG = "PopupPicker";

    protected float mScreenDensity;

    protected PopupWindow mPopupWindow;
    protected View mParent;
    protected Object mAnchor;
    protected View mContentView;
    protected RecyclerView mRecyclerView;
    protected Context context;

    public PopupPicker(Context context, View parent, int popupAnimStyle) {
        this.context = context;
        mScreenDensity = DisplayUtil.getScreenDensity(context);
        mParent = parent;

        mContentView = LayoutInflater.from(context).inflate(R.layout.view_rv_popup_picker, null);
        mRecyclerView = (RecyclerView) mContentView.findViewById(R.id.rv_popup_picker);
        mPopupWindow = new PopupWindow(mContentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mContentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 1) {
                    if (mPopupWindow != null && mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
        mPopupWindow.setAnimationStyle(popupAnimStyle);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
    }

    public void setAnchor(Object anchor) {
        mAnchor = anchor;
    }

    public abstract void updateAnchor();

    public abstract void show();

    public abstract void pickForUI(int index);

    public abstract int getPickedIndex();

    public void dismiss() {
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mPopupWindow.isShowing();
    }

}
