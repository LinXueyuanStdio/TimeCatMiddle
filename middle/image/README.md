# 图片相关的中间层

提供统一的设置项风格和统一的继承与实现方案，减少重复的设置页编写工作。

## 业务

提供一个 BaseImageSelectorActivity，允许以行级别的代码来配置一个九宫格选择器。

# BaseImageSelectorActivity 的使用

1. 继承 BaseImageSelectorActivity 写自己的 Activity，记得加入 AndroidManifest.xml。

