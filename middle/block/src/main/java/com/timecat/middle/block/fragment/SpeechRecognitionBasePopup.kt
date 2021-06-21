package com.timecat.middle.block.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.iflytek.cloud.*
import com.timecat.component.setting.DEF
import com.timecat.middle.block.R
import com.timecat.middle.block.util.JsonParser
import com.timecat.middle.block.view.recording.AudioWaveView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import razerdp.basepopup.BasePopupWindow
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-26
 * @description null
 * @usage null
 */
class SpeechRecognitionBasePopup(
    context: Context
) : BasePopupWindow(context, MATCH_PARENT, WRAP_CONTENT) {
    constructor(
        context: Context,
        listener: Listener
    ) : this(context) {
        this.listener = listener
        setContentView(R.layout.view_popup_speech_recognition)
    }

    interface Listener {
        fun onResult(result: String, path: String)
        fun path(): String
    }

    // 语音听写对象
    private var mIat: SpeechRecognizer? = null
    // 用HashMap存储听写结果
    private val mIatResults: HashMap<String?, String> = LinkedHashMap()
    // 函数调用返回值
    private var ret = 0
    // 引擎类型
    private val mEngineType = SpeechConstant.TYPE_CLOUD

    private lateinit var text: TextView
    private lateinit var waveView: AudioWaveView
    private lateinit var listener: Listener
    private lateinit var path: String

    override fun onViewCreated(view: View) {
        text = view.findViewById(R.id.text)
        waveView = view.findViewById(R.id.audioWave)
        path = listener.path()
        text.text = "初始化中..."
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.Main) {
                SpeechUtility.createUtility(context, SpeechConstant.APPID + "=5b930d00")
                Setting.setLocationEnable(false)
            }
            withContext(Dispatchers.Main) {
                mIat = SpeechRecognizer.createRecognizer(context, mInitListener)
            }
        }

        onDismissListener = object : OnDismissListener() {
            override fun onDismiss() {
                safeDismiss()
            }
        }
    }

    /**
     * 初始化监听器。
     */
    private val mInitListener = InitListener { code ->
        LogUtils.e("SpeechRecognizer init() code = $code")
        if (code != ErrorCode.SUCCESS) {
            showTip("初始化失败，错误码：$code")
            text.postDelayed({
                dismiss()
            }, 500)
        } else {
            showTip("正在倾听...")
            startListener()
        }
    }

    private fun startListener() {
        LogUtils.e("checkSoIsInstallSucceed")
        checkSoIsInstallSucceed()
        mIatResults.clear()
        // 设置参数
        setParam()// 不显示听写对话框
        mIat?.let {
            LogUtils.e("startListening")
            ret = it.startListening(mRecognizerListener)
            waveView.startView()
            if (ret != ErrorCode.SUCCESS) {
                LogUtils.e("startListening FAIL")
                showTip("听写失败,错误码：$ret")
                text.postDelayed({
                    dismiss()
                }, 500)
            } else {
                LogUtils.e("startListening SUCCESS")
                showTip("开始听写")
            }
        }
    }

    private fun checkSoIsInstallSucceed() {
        if (null == mIat) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            showTip("创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化")
            return
        }
    }

    private fun showTip(str: String) {
        text.text = str
    }

    /**
     * 参数设置
     *
     * @param
     * @return
     */
    private fun setParam() { // 清空参数
        LogUtils.e("setParam")
        mIat?.let {
            LogUtils.e("setParam")

            it.setParameter(SpeechConstant.PARAMS, null)
            // 设置听写引擎
            it.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType)
            // 设置返回结果格式
            it.setParameter(SpeechConstant.RESULT_TYPE, "json")
            val lag = DEF.speech().getString(
                "iat_language_preference",
                "mandarin"
            )
            if (lag == "en_us") { // 设置语言
                it.setParameter(SpeechConstant.LANGUAGE, "en_us")
            } else {
                // 设置语言
                it.setParameter(SpeechConstant.LANGUAGE, "zh_cn")
                // 设置语言区域
                it.setParameter(SpeechConstant.ACCENT, lag)
            }
            // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
            it.setParameter(
                SpeechConstant.VAD_BOS,
                DEF.speech().getString("iat_vadbos_preference", "4000")
            )
            // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
            it.setParameter(
                SpeechConstant.VAD_EOS,
                DEF.speech().getString("iat_vadeos_preference", "1000")
            )
            // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
            it.setParameter(
                SpeechConstant.ASR_PTT,
                DEF.speech().getString("iat_punc_preference", "1")
            )
            // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
            // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
            it.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
            it.setParameter(SpeechConstant.ASR_AUDIO_PATH, path)
        }
    }

    /**
     * 听写监听器。
     */
    private val mRecognizerListener: RecognizerListener = object : RecognizerListener {
        override fun onBeginOfSpeech() { // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话")
        }

        override fun onError(error: SpeechError) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            showTip(error.getPlainDescription(true))
            waveView.stopView()
            text.postDelayed({
                dismiss()
            }, 500)
        }

        override fun onEndOfSpeech() { // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("正在解析...")
        }

        override fun onResult(results: RecognizerResult, isLast: Boolean) {
            if (isLast) {
                waveView.stopView()
                listener.onResult(printResult(results), path)
                dismiss()
            }
        }

        override fun onVolumeChanged(volume: Int, data: ByteArray) {
            showTip("正在倾听，音量大小：$volume")
            val mVolumeList = waveView.getmVolumeList()
            if (mVolumeList != null && mVolumeList.size > 180) {
                mVolumeList.remove(0);
            }
            mVolumeList.add(volume);  //需要限制长度。
        }

        override fun onEvent(
            eventType: Int,
            arg1: Int,
            arg2: Int,
            obj: Bundle?
        ) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    }

    private fun printResult(results: RecognizerResult) :String{
        val text: String = JsonParser.parseIatResult(results.resultString)
        var sn: String? = null
        // 读取json结果中的sn字段
        try {
            val resultJson = JSONObject(results.resultString)
            sn = resultJson.optString("sn")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        mIatResults[sn] = text
        val resultBuffer = StringBuffer()
        for (key in mIatResults.keys) {
            resultBuffer.append(mIatResults[key])
        }
        val t = resultBuffer.toString()
        this.text.text = t
        LogUtils.e(t)
        return t
    }


    private fun safeDismiss() {
        waveView.stopView()
        mIat?.let {
            if (it.isListening) it.cancel()
            it.destroy()
        }// 退出时释放连接
    }
}