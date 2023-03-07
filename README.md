# ADT

* 集成apktool可以快速实现包体的反编译和回编译
* 集成jar2dex、dex2jar，实现文件格式类型转换，可根据新旧dex快速生成增量包patch.dex
* 集成apksigner和zipalign，快速实现包体的签名和对齐
* 集成bundletool，快速将aab文件转化为apks
* 集成adb可快速安装apk和提取手机内的apk包体(需配置adb环境变量)


使用方法
1. 直接下载release中的release/adt.zip，解压到本地目录,双击adt.bat文件即可
2. 通过gradle的task命令生成      生成路径：**项目的根目录/release/adt.zip**
```
./gradlew release
```
提示:
请确保您的PC包含java 11环境并配置了环境变量
```
如果觉得本工具对你有帮助，不妨请作者喝杯奶茶😊😊😊
```

<img src="./tools/wx_payment_code.jpg" width = "300" height = "300" />
