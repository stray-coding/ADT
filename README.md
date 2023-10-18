# ADT

* 集成apktool可以快速实现包体的反编译和回编译
* 集成jar2dex、dex2jar、smali.jar、backsmali.jar实现dex、smali、jar文件格式相互转换
* 集成jarsigner、apksigner、zipalign，快速实现包体的签名和对齐
* 集成bundletool，快速将aab文件转化为apks、apk转换为aab(实用价值不大)
* 集成adb可快速安装apk和提取手机内的apk包体


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

<div>
    <img src="tools/base/ali_payment_code.jpg" width = "300" height = "300">
    <img src="tools/base/wx_payment_code.jpg" width = "300" height = "300">
</div>

