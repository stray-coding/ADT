package com.coding.compose.base

import java.awt.Component
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileSystemView

/**
 *  Date 2020/7/13 20:59
 *  author hdl
 * @emil: stray-coding@foxmail.com
 * @des::file chooser
 */
class FileChooser private constructor(
    parent: Component, mode: Int, title: String, filterArray: Array<String>, onFileSelectListener: OnFileSelectListener
) : JFileChooser() {


    companion object {
        private var path = FileSystemView.getFileSystemView().homeDirectory

        fun newInstance(parent: Component, mode: Int, title: String, onFileSelectListener: OnFileSelectListener) {
            FileChooser(parent, mode, title, arrayOf(""), onFileSelectListener)
        }

        fun newInstance(
            parent: Component, mode: Int, title: String, filterStr: String, onFileSelectListener: OnFileSelectListener
        ) {
            FileChooser(parent, mode, title, arrayOf(filterStr), onFileSelectListener)
        }

        fun newInstance(
            parent: Component,
            mode: Int,
            title: String,
            filterArray: Array<String>,
            onFileSelectListener: OnFileSelectListener
        ) {
            FileChooser(parent, mode, title, filterArray, onFileSelectListener)
        }
    }

    init {
        fileSelectionMode = mode
        currentDirectory = path
        fileFilter = MyFileFilter(filterArray)
        showDialog(parent, title)
        selectedFile?.let {
            if (it.isDirectory) {
                println("dir:${it.absolutePath}")
            } else if (it.isFile) {
                println("file:${it.absolutePath}")
            }
            path = it.parentFile
            onFileSelectListener.onSelected(it.absolutePath)
        }
    }

    interface OnFileSelectListener {
        fun onSelected(path: String)
    }

    class MyFileFilter(array: Array<String>) : FileFilter() {

        private var suffixArray = arrayOf("")

        init {
            suffixArray = array
        }

        override fun accept(f: File): Boolean {
            if (suffixArray.isEmpty()) return true
            if (f.isDirectory) return true
            for (item in suffixArray) {
                if (f.name.endsWith(item)) {
                    return true
                }
            }
            return false
        }

        override fun getDescription(): String {
            return if (suffixArray.isEmpty()) {
                "all files or dir"
            } else {
                var des = ""
                for (item in suffixArray) {
                    des += item
                }
                des
            }
        }
    }
}