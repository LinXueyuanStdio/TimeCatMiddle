package com.timecat.middle.block.ext

import android.app.Activity
import android.content.Context
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.service.ImageSelectService
import com.timecat.identity.service.ImageUploadService
import com.timecat.identity.service.UploadCallback

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/12
 * @description null
 * @usage null
 */
fun selectImg(activity: Context, uploader: ImageUploadService?, selector: ImageSelectService?, isAvatar: Boolean, setImg: (String) -> Unit) {
    if (activity is Activity) {
        val onSelect: (String) -> Unit = { path ->
            if (uploader == null) {
                setImg(path)
            } else {
                uploader.upload(activity, path, object : UploadCallback {
                    override fun onSuccess(url: String) {
                        setImg(url)
                    }

                    override fun onFail(e: String) {
                        setImg(path)
                    }
                })
            }
        }
        if (isAvatar) {
            selector?.selectAvatar(activity, object : UploadCallback {
                override fun onSuccess(url: String) {
                    onSelect(url)
                }

                override fun onFail(e: String) {
                }
            })
        } else {
            selector?.selectImage(activity, object : UploadCallback {
                override fun onSuccess(url: String) {
                    onSelect(url)
                }

                override fun onFail(e: String) {
                }
            })
        }
    } else {
        ToastUtil.w_long("悬浮窗暂不支持选择图片")
    }
}