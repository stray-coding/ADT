package com.coding.utils

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

    fun run(cmd: String) {
        run(cmd, null)
    }

    fun run(cmd: String, callback: OnResultListener?) {
        println("cmd:$cmd")
        val p: Process
        try {
            p = Runtime.getRuntime().exec(cmd)
            dealStdoutResult(p, callback)
            dealStderrResult(p, callback)
//            p.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dealStdoutResult(p: Process, callback: OnResultListener?) {
        try {
            p.inputStream.use {
                val reader = p.inputStream.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    callback?.onStdout(line ?: "")
                    println("stdout:$line")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun dealStderrResult(p: Process, callback: OnResultListener?) {
        try {
            p.errorStream.use {
                val reader = p.errorStream.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    callback?.onStdErr(line ?: "")
                    println("stderr:$line")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface OnResultListener {
        fun onStdout(msg: String)
        fun onStdErr(err: String)
    }
}