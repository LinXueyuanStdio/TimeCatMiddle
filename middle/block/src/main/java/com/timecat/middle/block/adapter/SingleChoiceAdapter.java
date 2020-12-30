package com.timecat.middle.block.adapter;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.timecat.middle.block.base.BaseViewHolder;

/**
 * Created by ywwynm on 2015/8/19. Adapter to offer single select for items.
 */
public abstract class SingleChoiceAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    public static final String TAG = "SingleChoiceAdapter";

    protected int mPickedPosition = -1;

    @Override
    public abstract BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public abstract void onBindViewHolder(BaseViewHolder viewHolder, int position);

    @Override
    public abstract int getItemCount();

    public void pick(int position) {
        mPickedPosition = position;
        notifyDataSetChanged();
    }

    public int getPickedPosition() {
        return mPickedPosition;
    }
}
