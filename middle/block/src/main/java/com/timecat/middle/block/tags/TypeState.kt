package com.timecat.middle.block.tags

import com.timecat.identity.data.base.GOAL
import com.timecat.identity.data.base.HABIT
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.base.REMINDER
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/20
 * @description null
 * @usage null
 */
enum class TypeState(val title: String, val type: Int, val subType: Int) {
    ContainerUniversal("通用文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_UNIVERSAL),
    ContainerRecord("记录文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_RECORD),
    ContainerDatabase("数据库文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_DATABASE),
    ContainerNovel("小说文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_NOVEL),
    ContainerAccount("用户文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_ACCOUNT),
    ContainerMessage("消息文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_MESSAGE),
    ContainerAbout("公告文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_ABOUT),
    ContainerTag("标签文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_TAG),
    ContainerTopic("话题文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_TOPIC),
    ContainerContainer("文件夹的文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_CONTAINER),
    ContainerLeaderboard("排行榜文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_LEADER_BOARD),
    ContainerApp("App文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_APP),
    ContainerComment("评论文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_COMMENT),
    ContainerRecommend("推荐文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_RECOMMEND),
    ContainerConversation("对话文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_CONVERSATION),
    ContainerPage("分割符文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_DIVIDER),
    ContainerFocus("关注文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_FOCUS),
    ContainerFolder("路径文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_PATH),
    ContainerScript("文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_CODE),
    ContainerMoment("文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_MOMENT),
    ContainerDialog("文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_DIALOG),
    ContainerPlugin("文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_PLUGIN),
    ContainerMediaImage("图片文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_IMAGE),
    ContainerMediaUrl("网页书签文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_URL),
    ContainerMediaAudio("歌单", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_AUDIO),
    ContainerMediaVideo("影单", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_VIDEO),
    ContainerMediaCode("代码", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_CODE),
    ContainerMediaFile("文件夹", BLOCK_CONTAINER, CONTAINER_BLOCK_MEDIA_FILE),
    RecordNote("笔记", BLOCK_RECORD, NOTE),
    RecordReminder("提醒", BLOCK_RECORD, REMINDER),
    RecordHabit("习惯", BLOCK_RECORD, HABIT),
    RecordGoal("目标", BLOCK_RECORD, GOAL),
}