package com.timecat.middle.image

import android.app.Activity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/29
 * @description null
 * @usage null
 */
interface ImageSelectService {
    fun selectAvatar(activity: Activity, onSuccess: (String) -> Unit)
    fun selectImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)
    fun selectImage(activity: Activity, onSuccess: (String) -> Unit)

    //拍照
    fun takeOnePhoto(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)

    //本地相册
    fun selectOneLocalImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)

    //内置图标
    fun selectOneLocalIcon(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)

    //随机图标
    fun selectOneRandomImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)

    //我的在线相册
    fun selectOneOnlineImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit)
}