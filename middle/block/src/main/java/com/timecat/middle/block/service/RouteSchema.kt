package com.timecat.middle.block.service

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/21
 * @description null
 * @usage null
 */
object RouteSchema {
    /**
     * 本地
     * 1. world://timecat.local?db={space-dbPath}&recordId={record-uuid}
     * space-uuid 用于提供 IDatabase
     * record-uuid 交给下一步 Path 用于加载容器符文
     *
     * 2. world://timecat.local?recordId={record-uuid}
     * 不提供 space-uuid 则为默认主数据库
     */
    const val LocalHost = "timecat.local"
    const val LocalHostUrl = "${DNS.SCHEMA}://$LocalHost"

    const val LocalTocHost = "toc.timecat.local"
    const val localTocUrl = "${DNS.SCHEMA}://$LocalTocHost"

    const val LocalPluginHost = "plugin.timecat.local"
    const val LocalPluginUrl = "${DNS.SCHEMA}://$LocalPluginHost"

    /**
     * 在线资源
     * world://timecat.online/space/{space-uuid}/record/{record-uuid}
     * world://official.timecat.online?redirect={url = world://xxx}
     */
    /**
     * 在线地图的目录
     */
    const val OnlineTocHost = "toc.timecat.online"
    const val onlineTocUrl = "${DNS.SCHEMA}://${OnlineTocHost}"

    /**
     * 在线超空间内容
     */
    const val OnlineHost = "timecat.online"
    const val OnlineHostUrl = "${DNS.SCHEMA}://${OnlineHost}"

    /**
     * 官方在线超空间目录列表
     */
    const val OnlineOfficialHost = "official.timecat.online"
    const val onlineOfficialUrl = "${DNS.SCHEMA}://${OnlineOfficialHost}"

    /**
     * 我的在线，即当前登录用户的在线超空间目录列表
     */
    const val OnlineMineHost = "mine.timecat.online"
    const val onlineMineUrl = "${DNS.SCHEMA}://${OnlineMineHost}"

}