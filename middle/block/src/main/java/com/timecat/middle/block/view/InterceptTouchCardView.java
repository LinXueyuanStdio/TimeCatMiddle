package com.timecat.middle.block.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.cardview.widget.CardView;

/**
 * Created by ywwynm on 2015/9/18. A CardView that can interrupt touch event so that inner
 * RecyclerView cannot handle it.
 * <p>
 * updated on 2016/9/6 Because we want to let user have the ability to finish/unfinish checklist
 * item directly on thing card, we now can set if we should intercept touch events here.
 */
public class InterceptTouchCardView extends CardView {

    private boolean mShouldInterceptTouchEvent = true;

    public InterceptTouchCardView(Context context) {
        super(context);
    }

    public InterceptTouchCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptTouchCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setShouldInterceptTouchEvent(boolean shouldInterceptTouchEvent) {
        mShouldInterceptTouchEvent = shouldInterceptTouchEvent;
    }

    /**
     * if {@link #mShouldInterceptTouchEvent} is {@code true}, then we will intercept touch event so
     * that inner views cannot receive it. Otherwise, inner views can still handle their own touch
     * events.
     * <p>
     * If a ViewGroup contains a RecyclerView and has an OnTouchListener or something like that, touch
     * events will be directly delivered to inner RecyclerView and handled by it. As a result, parent
     * ViewGroup won't receive the touch event any longer. So this class is created to solve this
     * problem.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mShouldInterceptTouchEvent;
    }
}
