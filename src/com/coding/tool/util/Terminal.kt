package com.coding.tool.util

import java.io.File
import java.lang.Exception

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
     * get the working directory
     * */
    fun getCwd(): String {
        return System.getProperty("user.dir")
    }

    /**
     * change the working directory
     * */
    fun setCwd(dir: String) {
        val path = File(dir)
        if (!path.exists()) {
            println("set cwd failed,path is not exists ")
            return
        }
        if (path.isFile) {
            println("set cwd failed,cwd path cannot be filepath")
            return
        }
        System.setProperty("user.dir", dir)
    }

    fun run(cmd: String) {
        run(cmd, null)
    }

    fun run(cmd: String, callback: OnResultCallback?) {
        println("cmd:$cmd")
        val p: Process
        try {
            p = Runtime.getRuntime().exec(cmd)
            dealStdoutResult(p, callback)
            dealStderrResult(p, callback)
            p.outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dealStdoutResult(p: Process, callback: OnResultCallback?) {
        try {
            p.inputStream.use {
                val reader = p.inputStream.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    callback?.onStdout(line ?: "")
                    println("cmd stdout:$line")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dealStderrResult(p: Process, callback: OnResultCallback?) {
        try {
            p.errorStream.use {
                val reader = p.errorStream.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    callback?.onStdErr(line ?: "")
                    println("cmd stderr:$line")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnResultCallback {
        fun onStdout(msg: String)
        fun onStdErr(err: String)
    }
}