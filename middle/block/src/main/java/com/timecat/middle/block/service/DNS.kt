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
        val spaceId = uri.getQueryParameter(QUERY_SpaceId) ?: DEFAULT_QUERY_SpaceId
        val recordId = uri.getQueryParameter(QUERY_RecordId) ?: DEFAULT_QUERY_RecordId
        return spaceId to recordId
    }

    fun getRecordId(url: String): String {
        if (url.startsWith(SCHEMA)) {
            val uri = Uri.parse(url)
            return getRecordId(uri)
        } else {
            return url
        }
    }

    fun getRecordId(uri: Uri): String {
        return uri.getQueryParameter(QUERY_RecordId) ?: DEFAULT_QUERY_RecordId
    }

    fun getSpaceId(url: String): String {
        if (url.startsWith(SCHEMA)) {
            val uri = parse(url)
            return getSpaceId(uri)
        } else {
            return url
        }
    }

    fun getSpaceId(uri: Uri): String {
        return uri.getQueryParameter(QUERY_SpaceId) ?: DEFAULT_QUERY_SpaceId
    }

    fun buildUri(authority: String, spaceId: String, recordId: String, redirect: String): Uri.Builder {
        return buildUri(authority, spaceId, recordId)
            .appendQueryParameter(QUERY_Redirect, redirect)
    }

    fun buildUri(authority: String, spaceId: String, recordId: String): Uri.Builder {
        return buildUri(authority, spaceId)
            .appendQueryParameter(QUERY_RecordId, recordId)
    }

    fun buildUri(authority: String, spaceId: String): Uri.Builder {
        return buildUri(authority)
            .appendQueryParameter(QUERY_SpaceId, spaceId)
    }

    fun buildUri(authority: String): Uri.Builder {
        return Uri.EMPTY.buildUpon().scheme(SCHEMA).authority(authority)
    }
}