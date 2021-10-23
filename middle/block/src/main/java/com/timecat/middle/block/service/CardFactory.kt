package com.timecat.middle.block.service

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.*
import com.timecat.middle.block.adapter.UnknownCard
import com.timecat.middle.block.item.BaseRecordItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/23
 * @description null
 * @usage null
 */
interface CardFactoryService {
    fun buildCardFactory(): CardFactory
}

interface CardBuilder {
    suspend fun buildCards(
        records: List<RoomRecord>,
        context: Context,
        commonListener: ItemCommonListener
    ): MutableList<BaseRecordItem<*>> {
        val currentItems = mutableListOf<BaseRecordItem<*>>()
        for (record in records) {
            val card = buildOneCard(record, context, commonListener)
            currentItems.add(card)
        }
        return currentItems
    }

    suspend fun buildOneCard(
        record: RoomRecord,
        context: Context,
        commonListener: ItemCommonListener
    ): BaseRecordItem<*>
}

/**
 * 一类 [统一风格的] 卡片组
 */
abstract class CardFactory : CardBuilder {
    val cardBuilderFactoryMap: MutableMap<Int, CardBuilderFactory> = mutableMapOf()
    val unknownCardFactory = UnknownCardBuilderFactory()

    suspend fun loadFactory() {
        loadBasicFactory()
        //服务中的拓展类型 可能覆盖基础工厂
        loadFactoryFromService()
        //插件中的拓展类型 可能覆盖基础工厂
        loadFactoryFromPlugin()
    }

    suspend fun loadFactory(type: Int, cardBuilderFactory: CardBuilderFactory) {
        cardBuilderFactoryMap[type] = cardBuilderFactory
    }

    override suspend fun buildOneCard(
        record: RoomRecord,
        context: Context,
        commonListener: ItemCommonListener
    ): BaseRecordItem<*> {
        val type = record.type
        val factory = cardBuilderFactoryMap[type] ?: unknownCardFactory
        return factory.buildCard(record, context, commonListener)
    }

    //region plugin
    open suspend fun loadExtPluginBlockTypeService(): String {
        return ""
    }

    protected suspend fun loadFactoryFromPlugin() {
        val serviceName = loadExtPluginBlockTypeService()
        if (serviceName.isEmpty()) return
        val service = NAV.service(PluginBlockTypeService::class.java, serviceName) ?: return
        service.loadFactoryFromPlugin(object : LoadFactoryService {
            override suspend fun loadFactory(type: Int, cardBuilderFactory: CardBuilderFactory) {
                this@CardFactory.loadFactory(type, cardBuilderFactory)
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
            val service = NAV.service(BlockTypeService::class.java, serviceName) ?: continue
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

    open suspend fun buildFactoryFor_BLOCK_RECORD(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_DATABASE(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_NOVEL(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_MARKDOWN(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_MESSAGE(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_ABOUT(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_TAG(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_TOPIC(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_MEDIA(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_LEADER_BOARD(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_APP(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_COMMENT(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_RECOMMEND(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_CONVERSATION(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_CONTAINER(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_ACTIVITY(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_FOCUS(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_PATH(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_TASK(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_MOMENT(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_DIALOG(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_PLUGIN(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_LINK(): CardBuilderFactory = unknownCardFactory

    open suspend fun buildFactoryFor_BLOCK_BUTTON(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_FORUM(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_POST(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_PERMISSION(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_IDENTITY(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_ROLE(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_ITEM(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_MAIL(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_SHOP(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_SPACE(): CardBuilderFactory = unknownCardFactory
    open suspend fun buildFactoryFor_BLOCK_SKIN(): CardBuilderFactory = unknownCardFactory
    //endregion
}

class FunctionCardBuilderFactory(
    val func: suspend (
        record: RoomRecord,
        context: Context,
        commonListener: ItemCommonListener
    ) -> BaseRecordItem<*>
) : CardBuilderFactory {
    override suspend fun buildCard(record: RoomRecord, context: Context, commonListener: ItemCommonListener): BaseRecordItem<*> {
        return func(record, context, commonListener)
    }
}

class UnknownCardBuilderFactory : CardBuilderFactory {
    override suspend fun buildCard(record: RoomRecord, context: Context, commonListener: ItemCommonListener): BaseRecordItem<*> {
        return UnknownCard(record, context)
    }
}

/**
 * 针对某一符文类型的工厂
 */
interface CardBuilderFactory {
    suspend fun buildCard(
        record: RoomRecord,
        context: Context,
        commonListener: ItemCommonListener
    ): BaseRecordItem<*>
}

interface PluginBlockTypeService {
    suspend fun loadFactoryFromPlugin(loadFactory: LoadFactoryService)
}

interface LoadFactoryService {
    suspend fun loadFactory(type: Int, cardBuilderFactory: CardBuilderFactory)
}

interface BlockTypeService {
    fun forType(): Int
    suspend fun buildFactory(): CardBuilderFactory
}