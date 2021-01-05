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
class FileChooser private constructor(parent: Component, mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) :
    JFileChooser() {

    companion object {
        fun newInstance(mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileChooser(MainWindow.getInstance(), mode, title, "", onSelectListener)
        }

        fun newInstance(mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) {
            FileChooser(MainWindow.getInstance(), mode, title, filterStr, onSelectListener)
        }

        fun newInstance(parent: Component, mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileChooser(parent, mode, title, "", onSelectListener)
        }

        fun newInstance(parent: Component, mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) {
            FileChooser(parent, mode, title, filterStr, onSelectListener)
        }
    }

    init {
        fileSelectionMode = mode
        currentDirectory = FileSystemView.getFileSystemView().homeDirectory
        fileFilter = MyFileFilter(filterStr)
        showDialog(parent, title)
        val file = selectedFile
        val filePath = file.absolutePath
        if (file.isDirectory) {
            println("dir:$filePath")
        } else if (file.isFile) {
            println("file:$filePath")
        }
        onSelectListener.onSelected(filePath)
    }

    interface OnSelectListener {
        fun onSelected(path: String)
    }

    class MyFileFilter(str: String) : FileFilter() {

        private var suffix: String = ""

        init {
            suffix = str
        }

        override fun accept(f: File): Boolean {
            if (suffix.isEmpty()) return true
            return f.name.endsWith(suffix) || f.isDirectory
        }

        override fun getDescription(): String {
            return if (suffix.isEmpty()) "all files or dir" else suffix
        }
    }

}