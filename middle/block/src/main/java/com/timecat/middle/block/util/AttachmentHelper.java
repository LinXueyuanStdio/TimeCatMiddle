package com.timecat.middle.block.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.LinearLayout;

import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.timecat.extend.arms.BaseApplication;
import com.timecat.middle.block.R;
import com.timecat.middle.block.fragment.AttachmentInfoDialogFragment;
import com.timecat.middle.block.temp.Def;

import org.joda.time.DateTime;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ywwynm on 2015/9/23. Utils for attachment
 */
public class AttachmentHelper {

    public static final String TAG = "AttachmentHelper";
    public static final String SIGNAL = BaseApplication.getContext().getString(R.string.base_signal);
    public static final String SIZE_SEPARATOR = "`";
    public static final int IMAGE = 0;
    public static final int VIDEO = 1;
    public static final int AUDIO = 2;

    private AttachmentHelper() {
    }

    public static File createAttachmentFile(int type) {
        String folderName, fileType;
        if (type == IMAGE) {
            folderName = "images";
            fileType = ".jpg";
        } else if (type == VIDEO) {
            folderName = "videos";
            fileType = ".mp4";
        } else {
            folderName = "audios";
            fileType = ".wav";
        }

        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + fileType;
        return FileUtil.createFile(Def.Meta.APP_FILE_DIR + "/" + folderName, fileName);
    }

    public static void showVideoAttachmentInfoDialog(Activity activity, int accentColor,
                                                     String typePathName) {
        AttachmentInfoDialogFragment aidf = new AttachmentInfoDialogFragment();
        aidf.setAccentColor(accentColor);
        aidf.setItems(getAttachmentInfoVideo(activity, typePathName));
        aidf.show(activity.getFragmentManager(), AttachmentInfoDialogFragment.TAG);
    }

    public static void showImageAttachmentInfoDialog(Activity activity, int accentColor,
                                                     String typePathName) {
        AttachmentInfoDialogFragment aidf = new AttachmentInfoDialogFragment();
        aidf.setAccentColor(accentColor);
        aidf.setItems(getAttachmentInfoImage(activity, typePathName));
        aidf.show(activity.getFragmentManager(), AttachmentInfoDialogFragment.TAG);
    }

    public static List<Pair<String, String>> getAttachmentInfoImage(Context context, String pathName) {
        List<Pair<String, String>> list = new ArrayList<>();
        File file = new File(pathName);
        String fst = context.getString(R.string.file_path);
        if (!file.exists()) {
            String sec = context.getString(R.string.file_path_not_existed);
            list.add(new Pair<>(fst, sec));
            return list;
        }

        String sec = file.getAbsolutePath();
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.file_size);
        sec = FileUtil.getFileSizeStr(file);
        list.add(new Pair<>(fst, sec));

        return getAttachmentInfoImage(list, context, pathName);
    }

    public static List<Pair<String, String>> getAttachmentInfoVideo(Context context, String pathName) {
        List<Pair<String, String>> list = new ArrayList<>();
        File file = new File(pathName);
        String fst = context.getString(R.string.file_path);
        if (!file.exists()) {
            String sec = context.getString(R.string.file_path_not_existed);
            list.add(new Pair<>(fst, sec));
            return list;
        }

        String sec = file.getAbsolutePath();
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.file_size);
        sec = FileUtil.getFileSizeStr(file);
        list.add(new Pair<>(fst, sec));

        return getAttachmentInfoVideo(list, context, pathName);
    }

    public static List<Pair<String, String>> getAttachmentInfoAudio(Context context, String pathName) {
        List<Pair<String, String>> list = new ArrayList<>();
        File file = new File(pathName);
        String fst = context.getString(R.string.file_path);
        if (!file.exists()) {
            String sec = context.getString(R.string.file_path_not_existed);
            list.add(new Pair<>(fst, sec));
            return list;
        }

        String sec = file.getAbsolutePath();
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.file_size);
        sec = FileUtil.getFileSizeStr(file);
        list.add(new Pair<>(fst, sec));

        return getAttachmentInfoAudio(list, context, pathName);
    }

    private static List<Pair<String, String>> getAttachmentInfoImage(
            List<Pair<String, String>> list, Context context, String pathName) {
        String fst = context.getString(R.string.image_size);
        int[] size = FileUtil.getImageSize(pathName);
        String sec = size[0] + " * " + size[1];
        list.add(new Pair<>(fst, sec));

        DateTime dateTime = FileUtil.getImageCreateTime(pathName);
        if (dateTime == null) {
            fst = context.getString(R.string.file_last_modify_time);
            File file = new File(pathName);
            sec = DateTimeUtil.getGeneralDateTimeStr(context, file.lastModified());
        } else {
            fst = context.getString(R.string.image_create_time);
            sec = dateTime.toString(DateTimeUtil.getGeneralDateTimeFormatPattern(context));
        }
        list.add(new Pair<>(fst, sec));

        return list;
    }

    private static List<Pair<String, String>> getAttachmentInfoVideo(
            List<Pair<String, String>> list, Context context, String pathName) {
        int[] size = FileUtil.getVideoSize(pathName);
        if (size == null) {
            return null;
        }

        String fst = context.getString(R.string.video_size);
        String sec = size[0] + " * " + size[1];
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.video_duration);
        long duration = FileUtil.getMediaDuration(pathName);
        sec = DateTimeUtil.getDurationBriefStr(duration);
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.video_create_time);
        DateTime dateTime = FileUtil.getVideoCreateTime(pathName);
        if (dateTime == null || dateTime.compareTo(new DateTime(1970, 1, 1, 0, 0)) < 0) {
            fst = context.getString(R.string.file_last_modify_time);
            File file = new File(pathName);
            sec = DateTimeUtil.getGeneralDateTimeStr(context, file.lastModified());
        } else {
            sec = dateTime.toString(DateTimeUtil.getGeneralDateTimeFormatPattern(context));
        }
        list.add(new Pair<>(fst, sec));

        return list;
    }

    private static List<Pair<String, String>> getAttachmentInfoAudio(
            List<Pair<String, String>> list, Context context, String pathName) {
        String fst = context.getString(R.string.file_last_modify_time);
        File file = new File(pathName);
        String sec = DateTimeUtil.getGeneralDateTimeStr(context, file.lastModified());
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.audio_duration);
        long duration = FileUtil.getMediaDuration(pathName);
        sec = DateTimeUtil.getDurationBriefStr(duration);
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.audio_bitrate);
        int bitrate = FileUtil.getAudioBitrate(pathName);
        sec = bitrate + " Kbps";
        list.add(new Pair<>(fst, sec));

        fst = context.getString(R.string.audio_sample_rate);
        int sampleRate = FileUtil.getAudioSampleRate(pathName);
        sec = sampleRate + " Hz";
        list.add(new Pair<>(fst, sec));

        return list;
    }

    public static boolean isImageFile(String postfix) {
        String[] postfixes = new String[]{"png", "jpg", "jpeg", "gif", "bmp", "webp"};
        return isInsideArray(postfixes, postfix);
    }

    public static boolean isVideoFile(String postfix) {
        String[] postfixes = new String[]{"3gp", "mp4", "webm", "mkv"};
        return isInsideArray(postfixes, postfix);
    }

    public static boolean isAudioFile(String postfix) {
        String[] postfixes = new String[]{"wav", "mp3", "3gp", "mp4", "aac", "flac", "mid", "xmf",
                "mxmf", "rtttl", "rtx", "ota", "imy", "ogg", "mkv"};
        return isInsideArray(postfixes, postfix);
    }

    private static boolean isInsideArray(String[] array, String value) {
        for (String s : array) {
            if (value.equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static int[] calculateImageSize(Context context, int itemSize) {
        int[] size = new int[2];
        Resources res = context.getResources();
        boolean isTablet = DisplayUtil.isTablet(context);
        int orientation = res.getConfiguration().orientation;
        int displayWidth = res.getDisplayMetrics().widthPixels;

        if (isTablet) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (itemSize < 5) {
                    size[0] = displayWidth / itemSize;
                    size[1] = displayWidth / 3;
                } else {
                    size[0] = displayWidth / 5;
                    size[1] = size[0];
                }
            } else {
                if (itemSize == 1) {
                    size[0] = displayWidth;
                    size[1] = displayWidth * 3 / 4;
                } else if (itemSize == 2) {
                    size[0] = displayWidth / 2;
                    size[1] = displayWidth * 3 / 4;
                } else {
                    size[0] = displayWidth / 3;
                    size[1] = size[0];
                }
            }
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (itemSize < 4) {
                    size[0] = displayWidth / itemSize;
                    size[1] = displayWidth / 3;
                } else {
                    size[0] = displayWidth / 4;
                    size[1] = size[0];
                }
            } else {
                if (itemSize == 1) {
                    size[0] = displayWidth;
                    size[1] = size[0] * 3 / 4;
                } else {
                    size[0] = displayWidth / 2;
                    size[1] = size[0];
                }
            }
        }
        return size;
    }

    public static void setImageRecyclerViewHeight(RecyclerView recyclerView, int itemSize, int maxSpan) {
        int height;
        int itemHeight = calculateImageSize(recyclerView.getContext(), itemSize)[1];
        if (itemSize <= maxSpan) {
            height = itemHeight;
        } else {
            height = itemHeight * (itemSize / maxSpan);
            if (itemSize % maxSpan != 0) {
                height += itemHeight;
            }
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.height = height;
        recyclerView.requestLayout();
    }

    public static void setAudioRecyclerViewHeight(RecyclerView recyclerView, int itemSize, int span) {
        float density = recyclerView.getContext().getResources().getDisplayMetrics().density;

        int rows = itemSize / span;
        if (itemSize % span != 0) {
            rows++;
        }
        int itemHeight = (int) (density * 56);
        if (DeviceUtil.hasLollipopApi()) {
            itemHeight += density * 8;
        }

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
        params.height = itemHeight * rows;
        recyclerView.requestLayout();
    }

}
