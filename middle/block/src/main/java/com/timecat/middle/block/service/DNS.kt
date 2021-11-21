package com.timecat.middle.block.service

import android.net.Uri
import com.timecat.data.room.space.Space

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/21
 * @description
 * 形如
 * world://{host}/{path}?redirect={ContainerService}&spacedId={dbPath}&recordId={uuid}
 * @usage null
 */
object DNS {
    const val SCHEMA = "world"
    const val type = -10000

    const val QUERY_Redirect = "redirect"
    const val QUERY_SpaceId = "spacedId"
    const val QUERY_RecordId = "recordId"

    val DEFAULT_QUERY_SpaceId: String
        get() = Space.default().dbPath
    const val DEFAULT_QUERY_RecordId: String = ""

    fun parse(url: String): Uri {
        return Uri.parse(url)
    }

    fun parsePath(url: String): Pair<String, String> {
        val uri = Uri.parse(url)
        return parsePath(uri)
    }

    fun parsePath(uri: Uri): Pair<String, String> {
        val dbPath = uri.getQueryParameter(QUERY_SpaceId) ?: DEFAULT_QUERY_SpaceId
        val recordId = uri.getQueryParameter(QUERY_RecordId) ?: DEFAULT_QUERY_RecordId
        return dbPath to recordId
    }

    fun buildUri(dbPath: String, recordId: String): Uri.Builder {
        return buildUri()
            .appendQueryParameter(QUERY_SpaceId, dbPath)
            .appendQueryParameter(QUERY_RecordId, recordId)
    }

    fun buildUri(): Uri.Builder {
        return Uri.EMPTY.buildUpon().scheme(SCHEMA)
    }
}