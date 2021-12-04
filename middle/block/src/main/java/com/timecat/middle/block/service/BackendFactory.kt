package com.timecat.middle.block.service

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/12/4
 * @description null
 * @usage null
 */
interface IBackend {
    suspend fun getPermission(context: Context, spaceId: String): CardPermission
    suspend fun getDatabase(context: Context, spaceId: String): IDatabase
}

interface BackendFactoryService {
    fun buildBackendFactory(): BackendFactory
}

interface BackendBuilder {
    suspend fun buildOneBackend(context: Context, type: String): IBackend
}

/**
 * 一类 [统一风格的] 卡片组
 */
abstract class BackendFactory : BackendBuilder {
    val naviBuilderFactoryMap: MutableMap<String, BackendBuilderFactory> = mutableMapOf()
    val unknownBackendFactory = UnknownBackendBuilderFactory()

    suspend fun loadFactory() {
        loadBasicFactory()
        //服务中的拓展类型 可能覆盖基础工厂
        loadFactoryFromService()
        //插件中的拓展类型 可能覆盖基础工厂
        loadFactoryFromPlugin()
    }

    suspend fun loadFactory(type: String, naviBuilderFactory: BackendBuilderFactory) {
        naviBuilderFactoryMap[type] = naviBuilderFactory
    }

    override suspend fun buildOneBackend(context: Context, type: String): IBackend {
        val factory = naviBuilderFactoryMap[type] ?: unknownBackendFactory
        return factory.buildBackend(context, type)
    }

    //region plugin
    open suspend fun loadExtPluginBlockTypeService(): String {
        return ""
    }

    protected suspend fun loadFactoryFromPlugin() {
        val serviceName = loadExtPluginBlockTypeService()
        if (serviceName.isEmpty()) return
        val service = NAV.service(PluginBlockTypeBackendService::class.java, serviceName) ?: return
        service.loadFactoryFromPlugin(object : LoadBackendFactoryService {
            override suspend fun loadFactory(type: String, naviBuilderFactory: BackendBuilderFactory) {
                this@BackendFactory.loadFactory(type, naviBuilderFactory)
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
            val service = NAV.service(BlockTypeBackendService::class.java, serviceName) ?: continue
            val type = service.forType()
            val factory = service.buildFactory()
            loadFactory(type, factory)
        }
    }
    //endregion

    //region basic
    protected suspend fun loadBasicFactory() {
        loadFactory(RouteSchema.LocalHost, buildFactoryFor_LocalHost())
        loadFactory(RouteSchema.OnlineHost, buildFactoryFor_OnlineHost())
    }

    open suspend fun buildFactoryFor_LocalHost(): BackendBuilderFactory = unknownBackendFactory
    open suspend fun buildFactoryFor_OnlineHost(): BackendBuilderFactory = unknownBackendFactory
    //endregion
}

class FunctionBackendBuilderFactory(
    val func: suspend (context: Context, spaceId: String) -> IBackend
) : BackendBuilderFactory {
    override suspend fun buildBackend(context: Context, type: String): IBackend {
        return func(context, type)
    }
}

class UnknownBackendBuilderFactory : BackendBuilderFactory {
    override suspend fun buildBackend(context: Context, type: String): IBackend {
        return EmptyBackend()
    }
}

class EmptyBackend : IBackend {
    override suspend fun getDatabase(context: Context, spaceId: String): IDatabase {
        return EmptyDatabase()
    }

    override suspend fun getPermission(context: Context, spaceId: String): CardPermission {
        return CardPermission.NoAccess
    }
}

/**
 * 针对某一符文类型的工厂
 */
interface BackendBuilderFactory {
    suspend fun buildBackend(context: Context, type: String): IBackend
}

interface PluginBlockTypeBackendService {
    suspend fun loadFactoryFromPlugin(loadFactory: LoadBackendFactoryService)
}

interface LoadBackendFactoryService {
    suspend fun loadFactory(type: String, naviBuilderFactory: BackendBuilderFactory)
}

interface BlockTypeBackendService {
    fun forType(): String
    suspend fun buildFactory(): BackendBuilderFactory
}