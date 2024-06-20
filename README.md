# SearchViewDialog
[![Maven Central](https://img.shields.io/maven-central/v/cn.lalaki/SearchViewDialog.svg?label=Maven%20Central&logo=sonatype)](https://central.sonatype.com/artifact/cn.lalaki/SearchViewDialog/) ![API: 21+ (shields.io)](https://img.shields.io/badge/API-21+-2f9b45?logo=android) ![License: Apache-2.0 (shields.io)](https://img.shields.io/badge/License-Apache--2.0-c02041?logo=apache)

[ [中文说明](README_zh_cn.md) | [English](#) ]

**A simple dialog box that supports pinyin search and multi-selection.**

## Prerequisites

+ SDK version 21 and above
+ kotlin

## Quick Start

1. Import

    + Use Gradle, or download AAR: [SearchViewDialog](https://github.com/lalakii/SearchViewDialog/releases)

    ```kotlin
    dependencies {
        implementation("cn.lalaki:SearchViewDialog:2.6")
    }
    ```

2. Code sample

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

## Demo

![img0](https://cdn.jsdelivr.net/gh/lalakii/SearchViewDialog/video/demo.gif?v=1.6)

## About

Generate electricity for love.

+ feedback：dazen@189.cn

