package view

import util.CMD
import util.ToolUtil
import view.dialog.DecompileDialog
import view.dialog.SignDialog
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout
import javax.swing.*


/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: 229101253@qq.com
 * @des:
 */
class MainWindow : JFrame() {
    init {
        title = "反编译工具"
        val jadxBtn = JButton("jadx")
        jadxBtn.addActionListener {
            CMD.CMD("echo %cd%")
            CMD.CMD(ToolUtil.getJadx())
        }
        val decompileBtn = JButton("反编译")
        decompileBtn.addActionListener {
            DecompileDialog()
        }
        val backToCompileBtn = JButton("回编译")
        backToCompileBtn.addActionListener {
            println("backToCompileBtn")
        }
        val signBtn = JButton("签名")
        signBtn.addActionListener {
            SignDialog()
        }
        val alignBtn = JButton("对齐")
        alignBtn.addActionListener {
            println("alignBtn")
        }
        val fileSelectBtn = JButton("文件选择")
        fileSelectBtn.addActionListener {
            val jfc = JFileChooser()
            jfc.fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
            jfc.showDialog(JLabel(), "选择")
            val file = jfc.selectedFile
            if (file.isDirectory) {
                println("文件夹:" + file.absolutePath)
            } else if (file.isFile) {
                println("文件:" + file.absolutePath)
            }
            println(jfc.selectedFile.name)
        }
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = FlowLayout()
        size = Dimension(500,75)
        setLocationRelativeTo(null)
        add(jadxBtn)
        add(decompileBtn)
        add(backToCompileBtn)
        add(signBtn)
        add(alignBtn)
        add(fileSelectBtn)
        isVisible = true
    }
}