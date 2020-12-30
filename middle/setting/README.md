# 设置表项中间层

提供一个 BaseSettingActivity，允许以行级别的代码来配置设置项。

提供统一的设置项风格和统一的继承与实现方案，减少重复的设置页编写工作。

# 使用

1. 继承 BaseSettingActivity 写自己的 Activity，记得加入 AndroidManifest.xml。

2. 实现方法 addSettingItems(container: ViewGroup)，将各个配置项加入容器。

3. 配置项种类有 HeadItem头部 NextItem下一步 SlideItem滑块范围 SwitchItem开关等，顺序编程即可。
