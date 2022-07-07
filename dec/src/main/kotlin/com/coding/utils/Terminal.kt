package com.coding.utils

import java.util.concurrent.TimeUnit

/**
 * @author: Coding.He
 * @date: 2021/1/4
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object Terminal {
    fun isWindows(): Boolean {
        return System.getProperty("os.name").contains("Windows")
    }

    /**
     * cmd /c dir 是执行完dir命令后关闭命令窗口。
     * cmd /k dir 是执行完dir命令后不关闭命令窗口。
     * cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。
     * cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
     */
    //自动把cmd分割  去除""串
    fun run(cmd: String, timeout: Long = 0, listener: OnStdoutListener? = null): Boolean {
        println("cmd string:$cmd")
        val list = mutableListOf<String>()
        cmd.split(" ").forEach {
            val trim = it.trim()
            if (trim.isNotEmpty()) {
                list.add(trim)
            }
        }
        return run(list, timeout, listener)
    }

    fun run(cmd: List<String>, timeout: Long = 0, listener: OnStdoutListener? = null): Boolean {
        println("cmd param list:$cmd")
        val pb = ProcessBuilder(cmd)
            //合并标准输出和标准错误
            .redirectErrorStream(true)
        val p: Process = pb.start()
        p.inputStream.use { ins ->
            val reader = ins.bufferedReader()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                println("stdout:$line")
                listener?.callback(line ?: "")
            }
        }
        if (timeout > 0) {
            return p.waitFor(timeout, TimeUnit.MILLISECONDS)
        }
        //判断code码
        val code = p.waitFor()
        if (code != 0) {
            println("$cmd run code:$code")
        }
        return code == 0
    }

    interface OnStdoutListener {
        fun callback(line: String)
    }
}