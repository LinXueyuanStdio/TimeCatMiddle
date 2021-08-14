package com.timecat.middle.block.service

import android.content.Context
import com.alibaba.fastjson.JSONObject

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/14
 * @description null
 * @usage null
 */
interface CreateDatabaseSchemaService {
    fun simpleDatabaseSchema(context: Context): JSONObject
}