package com.timecat.middle.block.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.timecat.element.alert.ToastUtil;
import com.timecat.component.commonsdk.utils.clipboard.ClipboardUtils;
import com.timecat.identity.readonly.Constants;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
public class CopyToClipboard {
    public static void copy(Context context,String text) {
        Intent intent = new Intent(Constants.BROADCAST_SET_TO_CLIPBOARD);
        intent.putExtra(Constants.BROADCAST_SET_TO_CLIPBOARD_MSG, text);
        context.sendBroadcast(intent);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            ClipboardUtils.setText(context, text);
            ToastUtil.ok("已复制");
        }, 100);
    }
}
