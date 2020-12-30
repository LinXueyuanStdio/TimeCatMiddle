package com.timecat.middle.block.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.timecat.middle.block.R;
import com.timecat.middle.block.adapter.VMAdapter;
import com.timecat.middle.block.adapter.VMHolder;


/**
 * Create by lzan13 on 2019/5/29 14:16
 *
 * 表情适配器
 */
public class IMEmotionRecyclerAdapter extends VMAdapter<IMEmotionItem, IMEmotionRecyclerAdapter.EmotionHolder> {

    private IMEmotionGroup mEmotionGroup;

    public IMEmotionRecyclerAdapter(Context context, IMEmotionGroup group) {
        super(context);
        mEmotionGroup = group;
        mDataList = mEmotionGroup.mEmotionItemList;
    }

    @NonNull
    @Override
    public EmotionHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View view = mInflater.inflate(R.layout.view_emotion_recycler_view_item, parent, false);
        return new EmotionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmotionHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        IMEmotionItem item = getItemData(position);
        if (mEmotionGroup.isInnerEmotion) {
            holder.mNameView.setVisibility(View.GONE);
            holder.mIconView.setImageResource(item.mResId);
            if (mEmotionGroup.isBigEmotion) {
                holder.mNameView.setVisibility(View.VISIBLE);
                holder.mNameView.setText(item.mDesc.replaceAll("[\\[\\]]", ""));
            }
        } else {
            holder.mNameView.setVisibility(View.VISIBLE);
            holder.mNameView.setText(item.mDesc.replaceAll("[\\[\\]]", ""));
            // TODO 从服务器下载的表情加载
        }
    }

    class EmotionHolder extends VMHolder {
        public ImageView mIconView;
        public TextView mNameView;

        public EmotionHolder(View itemView) {
            super(itemView);
            mIconView = itemView.findViewById(R.id.im_emotion_item_icon_iv);
            mNameView = itemView.findViewById(R.id.im_emotion_item_name_tv);
        }
    }
}
