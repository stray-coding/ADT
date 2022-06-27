package com.coding.ui

import com.coding.dec.ADT
import com.coding.dec.utils.SignUtils
import com.coding.tool.constants.Constants
import com.coding.utils.Terminal
import java.awt.GridLayout
import javax.swing.*


/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: stray-coding@foxmail.com
 * @des:
 */
class MainWindow private constructor() : JFrame() {

    companion object {
        private val mainWindow = MainWindow()
        fun getInstance(): MainWindow {
            SignUtils.initSignXml()
            return mainWindow
        }
    }

    init {
        title = "ADT"
        val jadxBtn = JButton("jadx")
        jadxBtn.addActionListener {
            ADT.openJadx()
        }

        val decompileBtn = JButton("decompile")
        decompileBtn.addActionListener {
            DecompileDialog.showDialog()
        }

        val dexBtn = JButton("dex")
        dexBtn.addActionListener {
            DEXDialog.showDialog()
        }

        val backToCompileBtn = JButton("backToApk")
        backToCompileBtn.addActionListener {
            FileChooser.newInstance(JFileChooser.DIRECTORIES_ONLY, "backToApk", object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    ADT.backToApk(path)
                }
            })
        }
        val signBtn = JButton("sign")
        signBtn.addActionListener {
            SignDialog.showDialog()
        }

        val aabBtn = JButton("aab")
        aabBtn.addActionListener {
            AABDialog.showDialog()
        }


        val adbBtn = JButton("adb")
        adbBtn.addActionListener {
            ADBDialog.showDialog()
        }

        defaultCloseOperation = EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        jMenuBar = createMenuBar()
        add(jadxBtn)
        add(decompileBtn)
        add(dexBtn)
        add(backToCompileBtn)
        add(signBtn)
        add(aabBtn)
        add(adbBtn)
        isVisible = true
        isResizable = false
    }

    private fun createMenuBar(): JMenuBar {
        val menuBar = JMenuBar()
        val authorItem = JMenuItem("author")
        val helpItem = JMenuItem("help")
        authorItem.addActionListener {
            if (Terminal.isWindows()) {
                Terminal.run("explorer ${Constants.Author_Url}")
            } else {
                Terminal.run("open ${Constants.Author_Url}")
            }
        }
        helpItem.addActionListener {
            Toast.showMsg(
                """
                please configure java environment variable before using!
                # JAVA
                export PATH=/Library/Java/JavaVirtualMachines/jdk1.8.0_251.jdk/Contents/Home/bin
                # JAVA END
            """.trimIndent()
            )
//            # Android
//            export PATH=~/Library/Android/sdk/platform-tools
//            export PATH=~/Library/Android/sdk/build-tools/29.0.3
//            # Android END
        }
        menuBar.add(authorItem)
        menuBar.add(helpItem)
        return menuBar
    }

}