package com.coding.tool.view

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
class FileChooser private constructor(parent: Component, mode: Int, title: String, filterArray: Array<String>, onSelectListener: OnSelectListener) :
        JFileChooser() {

    companion object {
        fun newInstance(mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileChooser(MainWindow.getInstance(), mode, title, arrayOf(""), onSelectListener)
        }

        fun newInstance(mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) {
            FileChooser(MainWindow.getInstance(), mode, title, arrayOf(filterStr), onSelectListener)
        }

        fun newInstance(parent: Component, mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileChooser(parent, mode, title, arrayOf(""), onSelectListener)
        }

        fun newInstance(parent: Component, mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) {
            FileChooser(parent, mode, title, arrayOf(filterStr), onSelectListener)
        }

        fun newInstance(parent: Component, mode: Int, title: String, filterArray: Array<String>, onSelectListener: OnSelectListener) {
            FileChooser(parent, mode, title, filterArray, onSelectListener)
        }
    }

    init {
        fileSelectionMode = mode
        currentDirectory = FileSystemView.getFileSystemView().homeDirectory
        fileFilter = MyFileFilter(filterArray)
        showDialog(parent, title)
        selectedFile?.let {
            if (it.isDirectory) {
                println("dir:${it.absolutePath}")
            } else if (it.isFile) {
                println("file:${it.absolutePath}")
            }
            onSelectListener.onSelected(it.absolutePath)
        }
    }

    interface OnSelectListener {
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
            return if (suffixArray.isEmpty()) "all files or dir" else suffixArray.toString()
        }
    }

}