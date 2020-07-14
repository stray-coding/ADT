package com.coding.tool.view

import java.io.File
import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.filechooser.FileFilter
import javax.swing.filechooser.FileSystemView

/**
 *  Date 2020/7/13 20:59
 *  author hdl
 *  Description:文件选择器
 */
class FileJfc private constructor(mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) : JFileChooser() {

    companion object {
        fun newInstance(mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileJfc(mode, title, onSelectListener)
        }

        fun newInstance(mode: Int, title: String, filterStr: String, onSelectListener: OnSelectListener) {
            FileJfc(mode, title, filterStr, onSelectListener)
        }
    }

    constructor(mode: Int, title: String, onSelectListener: OnSelectListener) : this(mode, title, "", onSelectListener)

    init {
        fileSelectionMode = mode
        currentDirectory = FileSystemView.getFileSystemView().homeDirectory
        fileFilter = MyFileFilter(filterStr)
        showDialog(JLabel(), title)
        val file = selectedFile
        val filePath = file.absolutePath
        if (file.isDirectory) {
            println("文件夹:$filePath")
        } else if (file.isFile) {
            println("文件:$filePath")
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
            return if (suffix.isEmpty()) "所有文件" else suffix
        }
    }

}