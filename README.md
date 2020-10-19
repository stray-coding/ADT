# decompile_tool反编译工具类
* 集成Jadx快速查看apk包体内容
* 集成apktool、可快速实现包体的反编译与汇编
* 集成jar2dex、dex2jar，实现文件格式类型的转换
* 集成apksigner、zipalign快速实现对包体的签名与对齐
* 通过新旧dex，生成补丁patch

# 使用方法
请确保你的PC至少含有java环境  
下载release中的decompile_tool.zip，然后解压到本地目录  
1.打开cmd,目录跳转至decompile_tool.jar所在目录
```$xslt
cd decompile_tool.jar所在目录
```
2.运行decompile_tool.jar文件
```
java -jar decompile_tool.jar
```

