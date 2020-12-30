# 权限

```
                    hierarchical part
        ┌───────────────────┴─────────────────────┐
                    authority               path
        ┌───────────────┴───────────────┐┌───┴────┐
  abc://username:password@example.com:123/path/data?key=value&key2=value2#fragid1
  └┬┘   └───────┬───────┘ └────┬────┘ └┬┘           └─────────┬─────────┘ └──┬──┘
scheme  user information     host     port                  query         fragment

  urn:example:mammal:monotreme:echidna
  └┬┘ └──────────────┬───────────────┘
scheme              path
```

## UI权限

### UI 元权限

UI 元素有一个ui id。
UI 元素之上的权限是一种操作。
UI的权限是对UI操作的权限。
权限id需要描述这个操作。

有权限 id 的 UI 元素默认不显示。

权限id动态分配，UI id 硬编码。

ui://abcdefg/?vis=1

### UI 混权限

ui://abcdefg/?vis=*

## API权限

## 路由权限

一个路由只要一个身份id就可以，其他由用户权限来校验

## 实现

UI、API、路由等需要进行权限控制等对象称为受控对象
受控对象只要做一件事情：声明身份id

val roles = user.getRoles()
val permission = roles.getPermission() //角色 生成 权限控制类（缓存）
val id = obj.id
permission.check(id) {
  onAllowed = { obj.visibility = View.VISIBLE }
  onBanned = { obj.visibility = View.GONE }
  onNotAllowedThenNext = { NAV.go(it) }
}

## 权限控制类什么时候更新

权限控制类的生命周期

初始化
on用户主动，付费、降级 -> 主动更新
on用户被动，被封号、被降级、被赋予 -> 被动更新，推送
结束

不断读取最新权限，每隔少至5min必须主动拉去最新权限

