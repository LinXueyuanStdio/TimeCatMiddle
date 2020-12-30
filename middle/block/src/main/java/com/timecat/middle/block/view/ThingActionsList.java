package com.timecat.middle.block.view;

import com.timecat.component.commonsdk.utils.override.LogUtil;

import java.util.ArrayList;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/14
 * @description A collection class to provide some convenient methods for undo/redo updating thing
 * @usage null
 */
public class ThingActionsList {

    private static final int DEFAULT_MAX_UNDO_TIMES = 60;

    private int mMaxUndoTimes;

    private ArrayList<ThingAction> mActions;

    private int mCurPosition;

    private AddActionCallback mAddActionCallback;

    public ThingActionsList() {
        this(DEFAULT_MAX_UNDO_TIMES);
        LogUtil.se("init ThingActionsList");
    }

    public ThingActionsList(int maxUndoTimes) {
        LogUtil.se("init ThingActionsList");
        mMaxUndoTimes = maxUndoTimes;
        mActions = new ArrayList<>(maxUndoTimes);
        mCurPosition = -1;
    }

    public void setAddActionCallback(AddActionCallback addActionCallback) {
        mAddActionCallback = addActionCallback;
    }

    public void addAction(ThingAction action) {
        int size = mActions.size();
        for (int i = mCurPosition + 1; i < size; size--) {
            mActions.remove(i);
        }

        mActions.add(action);
        mCurPosition++;

        if (mCurPosition >= mMaxUndoTimes) {
            mActions.remove(0);
            mCurPosition--;
        }

        if (mAddActionCallback != null) {
            mAddActionCallback.onAddAction();
        }
    }

    public boolean canUndo() {
        return mCurPosition >= 0;
    }

    public boolean canRedo() {
        return mCurPosition < mActions.size() - 1;
    }

    public ThingAction undo() {
        mCurPosition--;
        return mActions.get(mCurPosition + 1);
    }

    public ThingAction redo() {
        mCurPosition++;
        return mActions.get(mCurPosition);
    }

    public interface AddActionCallback {
        void onAddAction();
    }

}
