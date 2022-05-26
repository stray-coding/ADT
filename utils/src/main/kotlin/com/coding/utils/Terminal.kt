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

    /**
     * cmd /c dir 是执行完dir命令后关闭命令窗口。
     * cmd /k dir 是执行完dir命令后不关闭命令窗口。
     * cmd /c start dir 会打开一个新窗口后执行dir指令，原窗口会关闭。
     * cmd /k start dir 会打开一个新窗口后执行dir指令，原窗口不会关闭。
     */
    fun run(cmd: String): Int {
        return run(cmd, null)
    }

    fun run(cmd: String, callback: OnResultListener?): Int {
        val p: Process
        try {
            println("cmd:$cmd")
            p = Runtime.getRuntime().exec(cmd)
            p.dealStdoutResult(callback)
            p.dealStderrResult(callback)
            return p.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        //默认错误状态码
        return -404
    }

    //处理标准输入
    private fun Process.dealStdoutResult(callback: OnResultListener?) {
        Thread {
            this.inputStream.use { ins ->
                val reader = ins.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    println("stdout:$line")
                    callback?.onStdout(line ?: "")
                }
            }
        }.start()
    }

    //处理异常输出
    private fun Process.dealStderrResult(callback: OnResultListener?) {
        Thread {
            this.errorStream.use { errs ->
                val reader = errs.bufferedReader()
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    println("stderr:$line")
                    callback?.onStdErr(line ?: "")
                }
            }
        }.start()
    }

    interface OnResultListener {
        fun onStdout(msg: String)
        fun onStdErr(err: String)
    }
}