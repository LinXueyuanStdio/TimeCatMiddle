package com.timecat.middle.block.im;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-03
 * @description null
 * @usage null
 */

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.timecat.extend.arms.BaseApplication;
import com.vmloft.develop.library.tools.utils.VMUtils;

/**
 * Create by lzan13 on 2019/5/28 22:19
 *
 * IM 工具类
 */
public class IMUtils extends VMUtils {

    /**
     * 简单的发送一个本地广播
     */
    public static void sendLocalBroadcast(String action) {
        sendLocalBroadcast(new Intent(action));
    }

    /**
     * 简单的发送一个本地广播
     */
    public static void sendLocalBroadcast(Intent intent) {
        LocalBroadcastManager.getInstance(BaseApplication.getContext()).sendBroadcast(intent);
    }

    /**
     * IM 模块儿 Action 管理
     */
    public static class Action {

        /**
         * 收到新消息
         */
        public static String getNewMessageAction() {
            return actionPrefix() + "chat.new.message";
        }

        /**
         * 收到 CMD 新消息
         */
        public static String getCMDMessageAction() {
            return actionPrefix() + "chat.cmd.message";
        }

        /**
         * 收到新消息
         */
        public static String getUpdateMessageAction() {
            return actionPrefix() + "chat.update.message";
        }

        /**
         * 刷新会话
         */
        public static String getRefreshConversationAction() {
            return actionPrefix() + "chat.refresh.conversation";
        }

        /**
         * 通话状态改变
         */
        public static String getCallStatusChangeAction() {
            return actionPrefix() + "call.status.change";
        }

        /**
         * 连接状态改变
         */
        public static String getConnectionChangeAction() {
            return actionPrefix() + "connection.status.change";
        }

        /**
         * 广播 action 前缀，其实就是 App 包名
         */
        private static String actionPrefix() {
            return BaseApplication.getContext().getPackageName();
        }
    }
}
