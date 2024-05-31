# SearchViewDialog

[![Maven Central](https://img.shields.io/maven-central/v/cn.lalaki/SearchViewDialog.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/cn.lalaki/SearchViewDialog/) ![API: 21+ (shields.io)](https://img.shields.io/badge/API-21+-green) ![License: Apache-2.0 (shields.io)](https://img.shields.io/badge/license-Apache--2.0-brightgreen)

[ [中文说明](#) | [English](README.md) ]

**一个简易的对话框，支持拼音搜索以及多选。**

## 必要条件

+ SDK 版本 21 及以上
+ kotlin

## 快速开始

1. 导入

   + 使用Gradle，或者直接下载AAR [SearchViewDialog](https://github.com/lalakii/SearchViewDialog/releases)

    ```kotlin
    dependencies {
        implementation("cn.lalaki:SearchViewDialog:2.6")
    }
    ```

2. 代码示例

   ```kotlin
   import cn.lalaki.dialog.SearchViewDialog
   import cn.lalaki.dialog.DataModel
           
   val list = mutableListOf<DataModel>()
   list.add(DataModel(1, "Java"))
   list.add(DataModel(2, "Kotlin"))
   list.add(DataModel(3, "易语言"))
   list.add(DataModel(4, "Perl", true))
   list.add(DataModel(5, "Delphi"))
   list.add(DataModel(6, "文言文", true))
   
   val dialog = SearchViewDialog(this)
   dialog.data = list
   dialog.title = "Choose language"
   
   //multiselect
   dialog.isMultiSelect = true
   
   dialog.listener = [SearchViewDialog.OnDataEventListener]
   
   dialog.show()
   
   
   ```

## 演示

![img0](https://cdn.jsdelivr.net/gh/lalakii/SearchViewDialog/video/demo.gif?v=1.6)

## 关于

为爱发电。

+ feedback：dazen@189.cn

