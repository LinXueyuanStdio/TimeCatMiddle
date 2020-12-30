package com.timecat.middle.block.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.timecat.middle.block.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class AddAttachmentPopup(
    var parent: View,
    var listener: Listener
) {
    interface Listener {
        fun startTakePhoto()
        fun startShootVideo()
        fun showRecordAudioDialog()
        fun startChooseMediaFile()
    }

    private val view: View = LayoutInflater.from(parent.context).inflate(
        R.layout.view_popup_add_attachment, null, false
    )
    private val popupWindow: PopupWindow
    private val mTvTakePhotoAsBt: View
    private val mTvShootVideoAsBt: View
    private val mTvRecordAudioAsBt: View
    private val mTvChooseMediaFilesAsBt: View

    init {
        popupWindow = PopupWindow(
            view,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        popupWindow.animationStyle = R.style.popup_window_animation
        popupWindow.showAtLocation(parent, 17, 0, 0)
        val root: View = view.findViewById(R.id.root)
        root.setOnClickListener { popupWindow.dismiss() }
        mTvTakePhotoAsBt = view.findViewById(R.id.tv_take_photo_as_bt)
        mTvShootVideoAsBt = view.findViewById(R.id.tv_shoot_video_as_bt)
        mTvRecordAudioAsBt = view.findViewById(R.id.tv_record_audio_as_bt)
        mTvChooseMediaFilesAsBt = view.findViewById(R.id.tv_choose_media_files_as_bt)
        mTvTakePhotoAsBt.setOnClickListener { startTakePhoto() }
        mTvShootVideoAsBt.setOnClickListener { startShootVideo() }
        mTvRecordAudioAsBt.setOnClickListener { showRecordAudioDialog() }
        mTvChooseMediaFilesAsBt.setOnClickListener { startChooseMediaFile() }
    }

    private fun startTakePhoto() {
        listener.startTakePhoto()
        popupWindow.dismiss()
    }

    private fun startShootVideo() {
        listener.startShootVideo()
        popupWindow.dismiss()
    }

    private fun showRecordAudioDialog() {
        listener.showRecordAudioDialog()
        popupWindow.dismiss()
    }

    private fun startChooseMediaFile() {
        listener.startChooseMediaFile()
        popupWindow.dismiss()
    }


}