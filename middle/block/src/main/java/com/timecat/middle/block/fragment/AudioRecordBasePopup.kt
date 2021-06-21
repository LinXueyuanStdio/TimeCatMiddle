package com.timecat.middle.block.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.core.content.ContextCompat
import com.timecat.middle.block.R
import com.timecat.middle.block.util.DisplayUtil
import com.timecat.middle.block.util.FileUtil
import com.timecat.middle.block.view.recording.AudioRecorder
import com.timecat.middle.block.view.recording.VoiceVisualizer
import razerdp.basepopup.BasePopupWindow
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class AudioRecordBasePopup(
    context: Context,
    var listener: Listener
) : BasePopupWindow(
    context, MATCH_PARENT, WRAP_CONTENT
) {
    interface Listener {
        fun addAttachment(attachmentPath: String)
    }

    init {
        setContentView(R.layout.view_base_popup_audio_record)
    }

    private val PREPARED = 0
    private val RECORDING = 1
    private val STOPPED = 2
    private val ANIM_DURATION = 360
    var screenDensity = 0f
    private var mState = PREPARED
    private var mRecorder: AudioRecorder? = null
    private var mFileToSave: File? = null
    private var mLlFileName: LinearLayout? = null
    private var mEtFileName: EditText? = null
    private var mChronometer: Chronometer? = null
    private var mVisualizer: VoiceVisualizer? = null
    private var mBase: View? = null
    private var mFabMain: ImageView? = null
    private var mIvReRecording: ImageView? = null
    private var mIvCancelRecording: ImageView? = null

    override fun onViewCreated(view: View) {
        screenDensity = DisplayUtil.getScreenDensity(context)
        mRecorder = AudioRecorder()
        mLlFileName = view.findViewById(R.id.ll_audio_file_name)
        mEtFileName = view.findViewById(R.id.et_audio_file_name)
        mChronometer = view.findViewById(R.id.chronometer_record_audio)
        mVisualizer = view.findViewById(R.id.voice_visualizer)
        mBase = view.findViewById(R.id.view_voice_visualizer_base)
        mFabMain = view.findViewById(R.id.fab_record_main)
        mIvReRecording = view.findViewById(R.id.iv_re_recording_audio)
        mIvCancelRecording = view.findViewById(R.id.iv_cancel_recording_audio)

        val accentColor = context.getResources().getColor(R.color.master_icon_view_special)
        mVisualizer!!.setRenderColor(accentColor)
        mBase!!.setBackgroundColor(accentColor)
        mEtFileName!!.highlightColor = DisplayUtil.getLightColor(accentColor, context)
        DisplayUtil.setSelectionHandlersColor(mEtFileName, accentColor)
        DisplayUtil.tintView(mEtFileName, ContextCompat.getColor(context, R.color.black_26p))

        mRecorder!!.link(mVisualizer)
        mRecorder!!.startListening()

        setEvents()
    }

    private fun setEvents() {
        val normalColor = ContextCompat.getColor(context, R.color.black_26p)
        mEtFileName!!.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DisplayUtil.tintView(
                    mEtFileName,
                    context.resources.getColor(R.color.master_icon_view_special)
                )
            } else {
                DisplayUtil.tintView(mEtFileName, normalColor)
            }
        }
        mEtFileName!!.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveFileAndLeave()
                return@setOnEditorActionListener true
            }
            false
        }
        mFabMain!!.setOnClickListener {
            when (mState) {
                PREPARED -> {
                    mRecorder!!.startRecording()
                    preparedToRecording()
                    mState = RECORDING
                }
                RECORDING -> {
                    mRecorder!!.stopListening(true)
                    mFileToSave = mRecorder!!.savedFile
                    mRecorder!!.startListening()
                    recordingToStopped()
                    mState = STOPPED
                }
                else -> saveFileAndLeave()
            }
        }
        mIvReRecording!!.setOnClickListener {
            if (mFileToSave != null) {
                FileUtil.deleteFile(mFileToSave!!.absolutePath)
            }
            mRecorder!!.stopListening(false)
            mRecorder!!.startListening()
            stoppedToPrepared()
            mState = PREPARED
        }
        mIvCancelRecording!!.setOnClickListener { cancelRecording() }
    }

    private fun cancelRecording() {
        if (mFileToSave != null) {
            FileUtil.deleteFile(mFileToSave!!.absolutePath)
        }
        mChronometer!!.stop()
        mRecorder!!.stopListening(false)
        mRecorder!!.release()

        FileUtil.deleteDirectory(FileUtil.TEMP_PATH + "/audio_raw")
        popupWindow.dismiss()
    }

    private fun preparedToRecording() {
        mChronometer!!.base = SystemClock.elapsedRealtime()
        mChronometer!!.start()
        mChronometer!!.animate().alpha(0.54f).duration = ANIM_DURATION.toLong()
        mVisualizer!!.animate().alpha(1.0f).duration = ANIM_DURATION.toLong()
        mBase!!.animate().alpha(1.0f).duration = ANIM_DURATION.toLong()
        mFabMain!!.setImageResource(R.drawable.act_stop_recording_audio)
        mFabMain!!.contentDescription = context.getString(R.string.cd_stop_record_audio)
    }

    private fun recordingToStopped() {
        mLlFileName!!.animate().translationY(screenDensity * 32).duration = ANIM_DURATION.toLong()
        val name = mFileToSave!!.name
        mEtFileName!!.setText(name.substring(0, name.length - 4))
        mChronometer!!.stop()
        mChronometer!!.animate().translationY(screenDensity * 72).duration = ANIM_DURATION.toLong()
        mVisualizer!!.animate().alpha(0.16f).duration = ANIM_DURATION.toLong()
        mBase!!.animate().alpha(0.16f).duration = ANIM_DURATION.toLong()
        mFabMain!!.backgroundTintList = ColorStateList.valueOf(
            Color.parseColor("#4CAF50")
        )
        mFabMain!!.setImageResource(R.drawable.act_save_audio)
        mIvReRecording!!.isClickable = true
        mIvCancelRecording!!.isClickable = true
        mIvReRecording!!.animate().alpha(1.0f).duration = ANIM_DURATION.toLong()
        mIvCancelRecording!!.animate().alpha(1.0f).duration = ANIM_DURATION.toLong()
        mFabMain!!.contentDescription = context.getString(R.string.cd_save_recorded_audio_file)
    }

    private fun stoppedToPrepared() {
        mLlFileName!!.animate().translationY(-screenDensity * 72).duration = ANIM_DURATION.toLong()
        mChronometer!!.base = SystemClock.elapsedRealtime()
        mChronometer!!.animate().alpha(0.26f).duration = ANIM_DURATION.toLong()
        mChronometer!!.animate().translationY(0f).duration = ANIM_DURATION.toLong()
        mFabMain!!.backgroundTintList = ColorStateList.valueOf(
            Color.parseColor("#FFFFFF")
        )
        mFabMain!!.setImageResource(R.drawable.act_start_recording_audio)
        mIvReRecording!!.isClickable = false
        mIvCancelRecording!!.isClickable = false
        mIvReRecording!!.animate().alpha(0f).duration =
            (ANIM_DURATION shr 4.toLong().toInt()).toLong()
        mIvCancelRecording!!.animate().alpha(0f).duration =
            (ANIM_DURATION shr 4.toLong().toInt()).toLong()
        mFabMain!!.contentDescription = context.getString(R.string.cd_start_record_audio)
    }

    private fun saveFileAndLeave() {
        val name = mEtFileName!!.text.toString()
        if (name.isEmpty()) {
            return
        }
        mRecorder!!.stopListening(false)
        mRecorder!!.release()
        val parent = mFileToSave!!.parentFile
        val fileToSave = File(parent, "$name.wav")
        var pathName = mFileToSave!!.absolutePath
        val renamed = mFileToSave!!.renameTo(fileToSave)
        if (renamed) {
            pathName = fileToSave.absolutePath
        }
        listener.addAttachment(pathName)
        FileUtil.deleteDirectory(FileUtil.TEMP_PATH + "/audio_raw")
        mEtFileName?.postDelayed({ dismiss() }, 300)
    }

}