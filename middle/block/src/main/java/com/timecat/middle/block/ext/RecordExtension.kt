package com.timecat.middle.block.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.data.system.model.SearchEngineUtil
import com.timecat.component.router.app.NAV
import com.timecat.component.setting.DEF
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.Constants
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.data.base.RENDER_TYPE_MARKDOWN
import com.timecat.identity.data.base.RENDER_TYPE_ORG
import com.timecat.middle.block.aggreement.ContentAgreement
import com.timecat.middle.block.security.PinLockController.needsLockCheck
import com.timecat.middle.block.security.sSecurityAppLockEnabled
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description RoomRecord的拓展
 * @usage null
 */
object TitleGetter {
    @JvmStatic
    fun getPrettyTitle(record:RoomRecord) : String{
        return record.prettyTitle
    }
}
val RoomRecord.prettyTitle: String
    get() {
        if (title.isNotEmpty()) return title
        if (ContentAgreement.isCheckListStr(content)) return "[清单]"
        if (render_type == RENDER_TYPE_ORG) return "[大纲]"
        if (render_type == RENDER_TYPE_MARKDOWN) return "[Markdown]"
        val line = content.substringBefore("\n")
        if (line.isEmpty()) return "<空白>"
        else if (line.length <= 30) return line
        else return line.substring(0, 30)
    }

fun RoomRecord.isNoteLockedButAppUnlocked(): Boolean {
    return this.isLocked() && !needsLockCheck() && sSecurityAppLockEnabled
}

fun actionSearch(context: Context, query:String) {
    LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_SEARCH)
    var isUrl = false
    var uri: Uri? = null
    var content = query
    try {
        val p = Pattern.compile(
            "^((https?|ftp|news):\\/\\/)?([a-z]([a-z0-9\\-]*[\\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\\/[a-z0-9_\\-\\.~]+)*(\\/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = p.matcher(content)
        if (!matcher.matches()) {
            val br = DEF.config().getInt(Constants.BROWSER_SELECTION, 0)
            val baseUrl = SearchEngineUtil.getInstance().searchEngines[br].url
            val encode = URLEncoder.encode(content, "utf-8")
            uri = Uri.parse(baseUrl +encode)
            isUrl = false
        } else {
            uri = Uri.parse(content)
            if (!content.startsWith("http")) {
                content = "http://${content}"
            }
            isUrl = true
        }

        val t = DEF.config().getBoolean(Constants.USE_LOCAL_WEBVIEW, true)
        val intent2Web: Intent
        if (t) {
            if (isUrl) {
                NAV.go(context, RouterHub.APP_WebActivity, "mUrl", content)
            } else {
                NAV.go(context, RouterHub.APP_WebActivity, "mQuery", content)
            }
        } else {
            intent2Web = Intent(Intent.ACTION_VIEW, uri)
            intent2Web.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent2Web)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        if (isUrl) {
            NAV.go(context, RouterHub.APP_WebActivity, "mUrl", content)
        } else {
            NAV.go(context, RouterHub.APP_WebActivity, "mQuery", content)
        }
    }
}