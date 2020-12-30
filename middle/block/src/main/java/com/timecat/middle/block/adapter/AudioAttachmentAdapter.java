package com.timecat.middle.block.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.timecat.middle.block.R;
import com.timecat.middle.block.base.BaseViewHolder;
import com.timecat.middle.block.util.DateTimeUtil;
import com.timecat.middle.block.util.FileUtil;
import com.timecat.middle.block.view.BarVisualizer;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

/**
 * Created by ywwynm on 2015/10/4. adapter for audio attachment
 */
public class AudioAttachmentAdapter extends
        RecyclerView.Adapter<AudioAttachmentAdapter.AudioCardViewHolder> {

    public static final String TAG = "AudioAttachmentAdapter";

    private Context context;

    private int mAccentColor;

    private boolean mEditable;

    private LayoutInflater mInflater;

    private List<String> mItems;

    private int mPlayingIndex = -1;
    private MediaPlayer mPlayer;

    private boolean mTakingScreenshot = false;
    private RemoveCallback mRemoveCallback;
    private AttachmentInfo attachmentInfo;

    public AudioAttachmentAdapter(
            Context context, int accentColor, boolean editable, List<String> items,
            RemoveCallback callback, AttachmentInfo attachmentInfo) {
        this.context = context;
        mAccentColor = accentColor;
        mEditable = editable;
        mInflater = LayoutInflater.from(context);
        mItems = items;

        this.attachmentInfo = attachmentInfo;
        mRemoveCallback = callback;
    }

    public void setTakingScreenshot(boolean takingScreenshot) {
        mTakingScreenshot = takingScreenshot;
        if (mPlayer != null && mPlayer.isPlaying()) {
            stopPlaying();
        }
        notifyDataSetChanged();
    }

    public List<String> getAudioItems() {
        return mItems;
    }

    public int getPlayingIndex() {
        return mPlayingIndex;
    }

    public void setPlayingIndex(int playingIndex) {
        mPlayingIndex = playingIndex;
    }

    @NotNull
    @Override
    public AudioCardViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new AudioCardViewHolder(mInflater.inflate(R.layout.attachment_audio, parent, false));
    }

    @Override
    public void onBindViewHolder(AudioCardViewHolder holder, int position) {
        String pathName = mItems.get(position);

        long duration = FileUtil.getMediaDuration(pathName);
        holder.tvSize.setText(DateTimeUtil.getDurationBriefStr(duration));

        if (mPlayingIndex == position && mPlayer != null) {
            holder.voiceVisualizer.setPlayer(mPlayer.getAudioSessionId());
        }

        if (mTakingScreenshot) {
            holder.ivFirst.setVisibility(View.VISIBLE);
            holder.ivSecond.setVisibility(View.GONE);
            holder.ivThird.setVisibility(View.GONE);
            holder.ivFirst.setImageResource(R.drawable.act_play);
        } else {
            Context context = holder.itemView.getContext();
            holder.ivSecond.setVisibility(View.VISIBLE);
            holder.ivThird.setVisibility(View.VISIBLE);
            if (mPlayingIndex == position) {
                holder.ivFirst.setVisibility(View.VISIBLE);
                if (mPlayer != null && mPlayer.isPlaying()) {
                    holder.ivFirst.setImageResource(R.drawable.act_pause);
                    holder.ivFirst
                            .setContentDescription(context.getString(R.string.cd_pause_play_audio_attachment));
                } else {
                    holder.ivFirst.setImageResource(R.drawable.act_play);
                    holder.ivFirst
                            .setContentDescription(context.getString(R.string.cd_play_audio_attachment));
                }
                holder.ivSecond.setImageResource(R.drawable.act_stop_playing_audio);
                holder.ivSecond
                        .setContentDescription(context.getString(R.string.cd_stop_play_audio_attachment));
            } else {
                if (mEditable) {
                    holder.ivFirst.setVisibility(View.VISIBLE);
                    holder.ivFirst.setImageResource(R.drawable.act_play);
                    holder.ivFirst
                            .setContentDescription(context.getString(R.string.cd_play_audio_attachment));
                    holder.ivSecond.setImageResource(R.drawable.delete_audio);
                    holder.ivSecond
                            .setContentDescription(context.getString(R.string.cd_delete_audio_attachment));
                } else {
                    holder.ivFirst.setVisibility(View.GONE);
                    holder.ivSecond.setImageResource(R.drawable.act_play);
                    holder.ivSecond
                            .setContentDescription(context.getString(R.string.cd_play_audio_attachment));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private void startPlaying(int index) {
        mPlayingIndex = index;
        String typePathName = mItems.get(index);
        Log.e("AudioCardViewHolder", typePathName);

        File file = new File(typePathName);
        Uri uri = Uri.parse(file.getAbsolutePath());
        Log.e("AudioCardViewHolder", uri.toString());

        mPlayer = MediaPlayer.create(context, uri);
        Log.e("AudioCardViewHolder", "player is not null");
        if (mPlayer == null) {
            return;
        }
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                final int index1 = mPlayingIndex;
                stopPlaying();
                notifyItemChanged(index1);
            }
        });
        mPlayer.start();
        Log.e("AudioCardViewHolder", "player start");
    }

    public void stopPlaying() {
        mPlayingIndex = -1;
        if (mPlayer == null) {
            return;
        }
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
    }

    public interface RemoveCallback {

        void onRemoved(int pos);
    }

    public interface AttachmentInfo {

        void showAudioAttachmentInfoDialog(int accentColor, String typePathName);
    }

    class AudioCardViewHolder extends BaseViewHolder {

        final CardView cv;
        final BarVisualizer voiceVisualizer;
        final TextView tvSize;
        final ImageView ivFirst;
        final ImageView ivSecond;
        final ImageView ivThird;

        AudioCardViewHolder(View itemView) {
            super(itemView);

            cv = f(R.id.cv_audio_attachment);
            voiceVisualizer = f(R.id.voice_visualizer);
            tvSize = f(R.id.tv_audio_size);
            ivFirst = f(R.id.iv_card_audio_first);
            ivSecond = f(R.id.iv_card_audio_second);
            ivThird = f(R.id.iv_card_audio_third);
            voiceVisualizer.setColor(Color.parseColor("#27ae60"));
            voiceVisualizer.setDensity(20);
            Drawable d = ContextCompat.getDrawable(context, R.drawable.act_show_attachment_info);
            Drawable d1 = d.mutate();
            d1.setColorFilter(Color.parseColor("#8A000000"), PorterDuff.Mode.SRC_ATOP);
            ivThird.setImageDrawable(d1);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePlay();
                }
            });

            ivThird.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String item = mItems.get(AudioCardViewHolder.this.getAdapterPosition());
                    if (attachmentInfo != null) {
                        attachmentInfo.showAudioAttachmentInfoDialog(mAccentColor, item);
                    }
                }
            });

            if (mEditable) {
                setEventsEditable();
            } else {
                setEventsUneditable();
            }
        }

        private void setEventsEditable() {
            ivFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    togglePlay();
                }
            });

            ivSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (mPlayingIndex == pos) {
                        stopPlaying();
                        notifyItemChanged(pos);
                    } else {
                        if (mRemoveCallback != null) {
                            mRemoveCallback.onRemoved(pos);
                        }
                    }
                }
            });
        }

        private void togglePlay() {
            int pos = getAdapterPosition();
            if (mPlayer != null && mPlayingIndex == pos) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                } else {
                    mPlayer.start();
                }
            } else {
                if (mPlayingIndex != -1) {
                    final int index = mPlayingIndex;
                    stopPlaying();
                    notifyItemChanged(index);
                }
                startPlaying(pos);
            }
            notifyItemChanged(pos);
        }

        private void setEventsUneditable() {
            ivFirst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayer == null) {
                        return;
                    }
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                    } else {
                        mPlayer.start();
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });

            ivSecond.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayer == null) {
                        return;
                    }
                    int pos = getAdapterPosition();
                    if (mPlayingIndex == pos) {
                        stopPlaying();
                    } else {
                        if (mPlayingIndex != -1) {
                            final int index = mPlayingIndex;
                            stopPlaying();
                            notifyItemChanged(index);
                        }
                        startPlaying(pos);
                        voiceVisualizer.setPlayer(mPlayer.getAudioSessionId());
                    }
                    notifyItemChanged(pos);
                }
            });
        }
    }

}
