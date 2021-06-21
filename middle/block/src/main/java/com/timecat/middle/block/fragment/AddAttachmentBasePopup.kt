package com.timecat.middle.block.fragment

import android.content.Context
import android.view.View
import com.timecat.middle.block.R
import razerdp.basepopup.BasePopupWindow

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-02
 * @description null
 * @usage null
 */
class AddAttachmentBasePopup(
    context: Context,
    var listener: Listener
) : BasePopupWindow(context, MATCH_PARENT, WRAP_CONTENT) {

    private lateinit var mTvTakePhotoAsBt: View
    private lateinit var mTvShootVideoAsBt: View
    private lateinit var mTvRecordAudioAsBt: View
    private lateinit var mTvChooseMediaFilesAsBt: View

    init {
        setContentView(R.layout.view_base_popup_add_attachment)
    }

    override fun onViewCreated(view: View) {
        mTvTakePhotoAsBt = view.findViewById(R.id.tv_take_photo_as_bt)
        mTvShootVideoAsBt = view.findViewById(R.id.tv_shoot_video_as_bt)
        mTvRecordAudioAsBt = view.findViewById(R.id.tv_record_audio_as_bt)
        mTvChooseMediaFilesAsBt = view.findViewById(R.id.tv_choose_media_files_as_bt)
        mTvTakePhotoAsBt.setOnClickListener { startTakePhoto() }
        mTvShootVideoAsBt.setOnClickListener { startShootVideo() }
        mTvRecordAudioAsBt.setOnClickListener { showRecordAudioDialog() }
        mTvChooseMediaFilesAsBt.setOnClickListener { startChooseMediaFile() }
    }

    interface Listener {
        fun startTakePhoto()
        fun startShootVideo()
        fun showRecordAudioDialog()
        fun startChooseMediaFile()
    }

    private fun startTakePhoto() {
        listener.startTakePhoto()
        dismiss()
    }

    private fun startShootVideo() {
        listener.startShootVideo()
        dismiss()
    }

    private fun showRecordAudioDialog() {
        listener.showRecordAudioDialog()
        dismiss()
    }

    private fun startChooseMediaFile() {
        listener.startChooseMediaFile()
        dismiss()
    }
}