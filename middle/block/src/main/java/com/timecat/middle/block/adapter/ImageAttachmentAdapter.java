package com.timecat.middle.block.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.timecat.middle.block.R;
import com.timecat.middle.block.base.BaseViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Created by ywwynm on 2015/9/23. Adapter for image attachments(including image, video and doodle)
 * of a thing.
 */
public class ImageAttachmentAdapter extends
        RecyclerView.Adapter<ImageAttachmentAdapter.ImageViewHolder> {

    private boolean mEditable;

    private Context mContext;

    private LayoutInflater mInflater;

    private List<String> mImageItems;
    private List<String> mVideoItems;
    private ClickCallback mClickCallback;
    private RemoveCallback mRemoveCallback;
    private boolean mTakingScreenshot = false;

    public ImageAttachmentAdapter(Context context,
                                  boolean editable,
                                  List<String> images,
                                  List<String> videos,
                                  ClickCallback clickCallback,
                                  RemoveCallback removeCallback) {
        mContext = context;
        mEditable = editable;
        mInflater = LayoutInflater.from(context);

        mImageItems = images;
        mVideoItems = videos;

        mClickCallback = clickCallback;
        mRemoveCallback = removeCallback;
    }

    public List<String> getImageItems() {
        return mImageItems;
    }

    public void setImageItems(List<String> imageItems) {
        mImageItems = imageItems;
    }

    public List<String> getVideoItems() {
        return mVideoItems;
    }

    public void setVideoItems(List<String> videoItems) {
        mVideoItems = videoItems;
    }

    public void setTakingScreenshot(boolean takingScreenshot) {
        mTakingScreenshot = takingScreenshot;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ImageViewHolder(mInflater.inflate(R.layout.attachment_image,
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull final ImageViewHolder holder, int position) {
        if (getItemCount() == 1) {
            holder.fl.getLayoutParams().width = FrameLayout.LayoutParams.MATCH_PARENT;
        }
        String pathName;
        if (position < mImageItems.size()) {
            pathName = mImageItems.get(position);
            holder.ivImage.setContentDescription(mContext.getString(R.string.cd_image_attachment));
            holder.ivDelete
                    .setContentDescription(mContext.getString(R.string.cd_delete_image_attachment));
            holder.ivVideoSignal.setVisibility(View.GONE);
        } else {
            pathName = mVideoItems.get(position);
            holder.ivImage.setContentDescription(mContext.getString(R.string.cd_video_attachment));
            holder.ivDelete
                    .setContentDescription(mContext.getString(R.string.cd_delete_video_attachment));
            holder.ivVideoSignal.setVisibility(View.VISIBLE);
        }
        Glide.with(holder.ivImage.getContext())
                .load(pathName)
                .apply(new RequestOptions().centerInside())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target,
                                                   DataSource dataSource, boolean isFirstResource) {
                        holder.ivDelete.setVisibility(View.VISIBLE);
                        holder.pbLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.ivImage);
        if (!mTakingScreenshot && mEditable) {
            holder.ivDelete.setVisibility(View.VISIBLE);
        } else {
            holder.ivDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mImageItems.size() + mVideoItems.size();
    }

    public interface ClickCallback {

        void onClick(View v, int pos);
    }

    public interface RemoveCallback {

        void onRemove(int pos);
    }

    class ImageViewHolder extends BaseViewHolder {

        final FrameLayout fl;
        final ImageView ivImage;
        final ImageView ivVideoSignal;
        final ImageView ivDelete;
        final ProgressBar pbLoading;

        ImageViewHolder(View itemView) {
            super(itemView);

            fl = f(R.id.fl_image_attachment);
            ivImage = f(R.id.iv_image_attachment);
            ivVideoSignal = f(R.id.iv_video_signal);
            ivDelete = f(R.id.iv_delete_image_attachment);
            pbLoading = f(R.id.pb_image_attachment);

            int pbColor = ContextCompat.getColor(mContext, R.color.master_colorAccent);
            pbLoading.getIndeterminateDrawable().setColorFilter(pbColor, PorterDuff.Mode.SRC_IN);

            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickCallback != null) {
                        mClickCallback.onClick(v, getAdapterPosition());
                    }
                }
            });

            if (mEditable) {
                ivDelete.setVisibility(View.VISIBLE);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mRemoveCallback != null) {
                            mRemoveCallback.onRemove(getAdapterPosition());
                        }
                    }
                });
            } else {
                ivDelete.setVisibility(View.GONE);
            }
        }
    }
}
