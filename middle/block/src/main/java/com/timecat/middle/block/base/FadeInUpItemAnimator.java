package com.timecat.middle.block.base;

import android.view.animation.Interpolator;

import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import eu.davidea.flexibleadapter.common.FlexibleItemAnimator;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/3/27
 * @description null
 * @usage null
 */
public class FadeInUpItemAnimator extends FlexibleItemAnimator {

    public FadeInUpItemAnimator() {
    }

    public FadeInUpItemAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder, final int index) {
        ViewCompat.animate(holder.itemView)
                .translationY(holder.itemView.getHeight() * .25f)
                .alpha(0)
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new FlexibleItemAnimator.DefaultRemoveVpaListener(holder))
                .start();
    }

    @Override
    protected boolean preAnimateAddImpl(final RecyclerView.ViewHolder holder) {
        holder.itemView.setTranslationY(holder.itemView.getHeight() * .25f);
        holder.itemView.setAlpha(0);
        return true;
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder, final int index) {
        ViewCompat.animate(holder.itemView)
                .translationY(0)
                .alpha(1)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder))
                .start();
    }

}