package com.timecat.middle.block.tags

import com.timecat.component.setting.DEF
import com.timecat.identity.data.base.ITaskStatus
import com.timecat.identity.data.base.TASK_MODE_INIT
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.identity.data.block.type.BLOCK_CONVERSATION
import com.timecat.identity.data.block.type.BLOCK_RECORD

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-06
 * @description 用于搜索数据库的状态模型
 * @usage null
 */
class DomainState(
    var status: Long = DEF.filter().getLong("DomainState", TASK_MODE_INIT),
    var type: MutableList<Int> = mutableListOf(BLOCK_RECORD, BLOCK_CONTAINER, BLOCK_CONVERSATION),
    var all: Boolean = true
) : ITaskStatus {
    override fun toString(): String {
        return "all=${all}, $type, ${statusDescription()}"
    }
    //region 2. ITaskStatus
    //region Status 用 16 进制管理状态
    override fun updateStatus(s: Long, yes: Boolean) {
        super.updateStatus(s, yes)
        DEF.filter().putLong("DomainState", status)
    }

    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.status = this.status or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.status = this.status and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.status and status != 0L
    }
    //endregion
    //endregion
}