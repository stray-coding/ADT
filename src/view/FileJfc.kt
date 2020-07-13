package view

import javax.swing.JFileChooser
import javax.swing.JLabel
import javax.swing.filechooser.FileSystemView

/**
 *  Date 2020/7/13 20:59
 *  author hdl
 *  Description:文件选择器
 */
class FileJfc private constructor(mode: Int, title: String, onSelectListener: OnSelectListener) : JFileChooser() {

    companion object {
        fun newInstance(mode: Int, title: String, onSelectListener: OnSelectListener) {
            FileJfc(mode, title, onSelectListener)
        }
    }

    init {
        fileSelectionMode = mode
        currentDirectory = FileSystemView.getFileSystemView().homeDirectory
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
}