package com.timecat.middle.block.service

import android.content.Context
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.SubTypeCard
import com.timecat.middle.block.adapter.TypeCard
import com.timecat.middle.block.adapter.TypeItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/23
 * @description null
 * @usage null
 */
abstract class CreateFactory : CreateBuilder {
    val factoryMap: MutableMap<Int, CreateBlockTypeService> = mutableMapOf()
    val subItemMap: MutableMap<Int, CreateBlockSubTypeService> = mutableMapOf()

    suspend fun loadFactory() {
        loadBasicFactory()
        //服务中的拓展类型 可能覆盖基础工厂
        loadFactoryFromService()
        //插件中的拓展类型 可能覆盖基础工厂
        loadFactoryFromPlugin()
    }

    suspend fun loadFactory(type: Int, factory: CreateBlockTypeService) {
        factoryMap[type] = factory
        subItemMap[type] = factory.buildFactory()
    }

    open suspend fun loadExtTypeFromService(): List<String> {
        return listOf()
    }

    protected suspend fun loadBasicFactory() {

    }

    protected suspend fun loadFactoryFromService() {
        val serviceNames = loadExtTypeFromService()
        if (serviceNames.isEmpty()) return
        for (serviceName in serviceNames) {
            val service = NAV.service(CreateBlockTypeService::class.java, serviceName) ?: continue
            val type = service.type()
            loadFactory(type, service)
        }
    }

    protected suspend fun loadFactoryFromPlugin() {

    }

    override suspend fun getTypeCards(context: Context, folder: RoomRecord?, listener: ItemCommonListener, onDismiss: () -> Unit): List<TypeCard> {
        val data = mutableListOf<TypeCard>()
        folder?.let {
            val cardsInFolder = wrap(
                TypeItem(
                    0, "在当前文件夹下创建（${folder.title}）",
                    "在当前浏览的目录下创建数据。所有数据只存储在当前文件夹下。"
                ), context, it, listener, onDismiss
            )
            data.add(cardsInFolder)
        }
        val forRoot = wrap(
            TypeItem(
                0, "在根目录下创建",
                "将在当前超空间的根目录（而不是当前浏览的目录）下创建数据。"
            ), context, null, listener, onDismiss
        )
        data.add(forRoot)
        return data
    }

    suspend fun wrap(type: TypeItem, context: Context, folder: RoomRecord?, listener: ItemCommonListener, onDismiss: () -> Unit): TypeCard {
        val typeCard = TypeCard(type)
        val forRoot = getTypeCardsForFolder(context, folder, listener, onDismiss)
        for (i in forRoot) {
            typeCard.addSubItem(i)
        }
        return typeCard
    }

    suspend fun getTypeCardsForFolder(context: Context, folder: RoomRecord?, listener: ItemCommonListener, onDismiss: () -> Unit): List<TypeCard> {
        val list = mutableListOf<TypeCard>()
        for (i in factoryMap.keys) {
            val typeItem = factoryMap[i]?.typeItem(folder) ?: continue
            val subItemService = subItemMap[i] ?: continue
            val items = subItemService.subItems(folder, listener)
            if (items.isNullOrEmpty()) continue
            val subTypeListener = object : SubTypeCard.Listener {
                override fun loadFor(subItem: SubItem) {
                    subItemService.create(context, subItem, folder, listener)
                    onDismiss()
                }

                override fun more(subItem: SubItem) {
                    NAV.go(subItem.helpUrl)
                }
            }
            val typeCard = TypeCard(typeItem).apply {
                items.forEach {
                    val card = SubTypeCard(it, context, subTypeListener)
                    addSubItem(card)
                }
            }
            list.add(typeCard)
        }
        return list
    }
}

interface CreateBlockSubTypeService {
    suspend fun subtype(): List<Int>
    suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem>  // can be empty
    fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInPage(context, subItem, parent, listener)
    }

    fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener)
    fun createInSlient(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val newRecord = RoomRecord.forName("")
        newRecord.type = subItem.type
        newRecord.subType = subItem.subType
        LogUtil.se("${subItem.type}, ${subItem.subType}")
        newRecord.parent = subItem.uuid
        listener.addNewItemToEndOfList(newRecord, listener)
    }

    fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener)
    fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener)
}

interface CreateBlockTypeService {
    fun type(): Int
    fun typeItem(parent: RoomRecord?): TypeItem
    suspend fun buildFactory(): CreateBlockSubTypeService
}

interface CreateBuilder {
    suspend fun getTypeCards(context: Context, folder: RoomRecord?, listener: ItemCommonListener, onDismiss: () -> Unit): List<TypeCard>
}
