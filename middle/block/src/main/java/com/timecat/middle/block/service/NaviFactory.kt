package com.timecat.middle.block.service

import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.*
import com.timecat.layout.ui.business.breadcrumb.Path

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/20
 * @description 针对于类型的加载服务
 * @usage null
 */
interface NaviFactoryService {
    fun buildNaviFactory(): NaviFactory
}

interface NaviBuilder {
    suspend fun buildOneNavi(parentPath: Path, spaceId: String, record: RoomRecord): Path
}

/**
 * 一类 [统一风格的] 卡片组
 */
abstract class NaviFactory : NaviBuilder {
    val naviBuilderFactoryMap: MutableMap<Int, NaviBuilderFactory> = mutableMapOf()
    val unknownNaviFactory = UnknownNaviBuilderFactory()

    suspend fun loadFactory() {
        loadBasicFactory()
        //服务中的拓展类型 可能覆盖基础工厂
        loadFactoryFromService()
        //插件中的拓展类型 可能覆盖基础工厂
        loadFactoryFromPlugin()
    }

    suspend fun loadFactory(type: Int, naviBuilderFactory: NaviBuilderFactory) {
        naviBuilderFactoryMap[type] = naviBuilderFactory
    }

    override suspend fun buildOneNavi(parentPath: Path, spaceId: String, record: RoomRecord): Path {
        val type = record.type
        val factory = naviBuilderFactoryMap[type] ?: unknownNaviFactory
        return factory.buildNavi(parentPath, spaceId, record)
    }

    //region plugin
    open suspend fun loadExtPluginBlockTypeService(): String {
        return ""
    }

    protected suspend fun loadFactoryFromPlugin() {
        val serviceName = loadExtPluginBlockTypeService()
        if (serviceName.isEmpty()) return
        val service = NAV.service(PluginBlockTypeNaviService::class.java, serviceName) ?: return
        service.loadFactoryFromPlugin(object : LoadNaviFactoryService {
            override suspend fun loadFactory(type: Int, naviBuilderFactory: NaviBuilderFactory) {
                this@NaviFactory.loadFactory(type, naviBuilderFactory)
            }
        })
    }
    //endregion

    //region service
    open suspend fun loadExtTypeFromService(): List<String> {
        return listOf()
    }

    protected suspend fun loadFactoryFromService() {
        val serviceNames = loadExtTypeFromService()
        if (serviceNames.isEmpty()) return
        for (serviceName in serviceNames) {
            val service = NAV.service(BlockTypeNaviService::class.java, serviceName) ?: continue
            val type = service.forType()
            val factory = service.buildFactory()
            loadFactory(type, factory)
        }
    }
    //endregion

    //region basic
    protected suspend fun loadBasicFactory() {
        loadFactory(BLOCK_RECORD, buildFactoryFor_BLOCK_RECORD())
        loadFactory(BLOCK_DATABASE, buildFactoryFor_BLOCK_DATABASE())
        loadFactory(BLOCK_NOVEL, buildFactoryFor_BLOCK_NOVEL())
        loadFactory(BLOCK_MARKDOWN, buildFactoryFor_BLOCK_MARKDOWN())
        loadFactory(BLOCK_MESSAGE, buildFactoryFor_BLOCK_MESSAGE())
        loadFactory(BLOCK_ABOUT, buildFactoryFor_BLOCK_ABOUT())
        loadFactory(BLOCK_TAG, buildFactoryFor_BLOCK_TAG())
        loadFactory(BLOCK_TOPIC, buildFactoryFor_BLOCK_TOPIC())
        loadFactory(BLOCK_MEDIA, buildFactoryFor_BLOCK_MEDIA())
        loadFactory(BLOCK_LEADER_BOARD, buildFactoryFor_BLOCK_LEADER_BOARD())
        loadFactory(BLOCK_APP, buildFactoryFor_BLOCK_APP())
        loadFactory(BLOCK_COMMENT, buildFactoryFor_BLOCK_COMMENT())
        loadFactory(BLOCK_RECOMMEND, buildFactoryFor_BLOCK_RECOMMEND())
        loadFactory(BLOCK_CONVERSATION, buildFactoryFor_BLOCK_CONVERSATION())
        loadFactory(BLOCK_CONTAINER, buildFactoryFor_BLOCK_CONTAINER())
        loadFactory(BLOCK_ACTIVITY, buildFactoryFor_BLOCK_ACTIVITY())
        loadFactory(BLOCK_FOCUS, buildFactoryFor_BLOCK_FOCUS())
        loadFactory(BLOCK_PATH, buildFactoryFor_BLOCK_PATH())
        loadFactory(BLOCK_TASK, buildFactoryFor_BLOCK_TASK())
        loadFactory(BLOCK_MOMENT, buildFactoryFor_BLOCK_MOMENT())
        loadFactory(BLOCK_DIALOG, buildFactoryFor_BLOCK_DIALOG())
        loadFactory(BLOCK_PLUGIN, buildFactoryFor_BLOCK_PLUGIN())
        loadFactory(BLOCK_LINK, buildFactoryFor_BLOCK_LINK())
        loadFactory(BLOCK_BUTTON, buildFactoryFor_BLOCK_BUTTON())
        loadFactory(BLOCK_FORUM, buildFactoryFor_BLOCK_FORUM())
        loadFactory(BLOCK_POST, buildFactoryFor_BLOCK_POST())
        loadFactory(BLOCK_PERMISSION, buildFactoryFor_BLOCK_PERMISSION())
        loadFactory(BLOCK_IDENTITY, buildFactoryFor_BLOCK_IDENTITY())
        loadFactory(BLOCK_ROLE, buildFactoryFor_BLOCK_ROLE())
        loadFactory(BLOCK_ITEM, buildFactoryFor_BLOCK_ITEM())
        loadFactory(BLOCK_MAIL, buildFactoryFor_BLOCK_MAIL())
        loadFactory(BLOCK_SHOP, buildFactoryFor_BLOCK_SHOP())
        loadFactory(BLOCK_SPACE, buildFactoryFor_BLOCK_SPACE())
        loadFactory(BLOCK_SKIN, buildFactoryFor_BLOCK_SKIN())
    }

    open suspend fun buildFactoryFor_BLOCK_RECORD(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_DATABASE(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_NOVEL(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_MARKDOWN(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_MESSAGE(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_ABOUT(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_TAG(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_TOPIC(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_MEDIA(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_LEADER_BOARD(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_APP(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_COMMENT(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_RECOMMEND(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_CONVERSATION(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_CONTAINER(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_ACTIVITY(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_FOCUS(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_PATH(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_TASK(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_MOMENT(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_DIALOG(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_PLUGIN(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_LINK(): NaviBuilderFactory = unknownNaviFactory

    open suspend fun buildFactoryFor_BLOCK_BUTTON(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_FORUM(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_POST(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_PERMISSION(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_IDENTITY(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_ROLE(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_ITEM(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_MAIL(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_SHOP(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_SPACE(): NaviBuilderFactory = unknownNaviFactory
    open suspend fun buildFactoryFor_BLOCK_SKIN(): NaviBuilderFactory = unknownNaviFactory
    //endregion
}

class FunctionNaviBuilderFactory(
    val func: suspend (parentPath: Path, spaceId: String, record: RoomRecord) -> Path
) : NaviBuilderFactory {
    override suspend fun buildNavi(parentPath: Path, spaceId: String, record: RoomRecord): Path {
        return func(parentPath, spaceId, record)
    }
}

class UnknownNaviBuilderFactory : NaviBuilderFactory {
    override suspend fun buildNavi(parentPath: Path, spaceId: String, record: RoomRecord): Path {
        return parentPath
    }
}

/**
 * 针对某一符文类型的工厂
 */
interface NaviBuilderFactory {
    suspend fun buildNavi(parentPath: Path, spaceId: String, record: RoomRecord): Path
}

interface PluginBlockTypeNaviService {
    suspend fun loadFactoryFromPlugin(loadFactory: LoadNaviFactoryService)
}

interface LoadNaviFactoryService {
    suspend fun loadFactory(type: Int, naviBuilderFactory: NaviBuilderFactory)
}

interface BlockTypeNaviService {
    fun forType(): Int
    suspend fun buildFactory(): NaviBuilderFactory
}