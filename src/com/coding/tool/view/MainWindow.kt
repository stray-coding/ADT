package com.coding.tool.view

import com.coding.tool.Proxy
import com.coding.tool.constants.Constants
import com.coding.tool.util.SignCfgUtil
import com.coding.tool.util.Suffix
import com.coding.tool.util.Terminal
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
            SignCfgUtil.initSignXml()
            return mainWindow
        }
    }

    init {
        title = "android decompile tool"
        val jadxBtn = JButton("jadx")
        jadxBtn.addActionListener {
            Proxy.openJadx()
        }

        val decompileBtn = JButton("decompile")
        decompileBtn.addActionListener {
            DecompileDialog.showDialog()
        }

        val dex2jarBtn = JButton("dex2jar")
        dex2jarBtn.addActionListener {
            FileChooser.newInstance(JFileChooser.FILES_ONLY, "dex2jar", Suffix.DEX, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.dex2jar(path)
                }
            })
        }

        val jar2dexBtn = JButton("jar2dex")
        jar2dexBtn.addActionListener {
            FileChooser.newInstance(JFileChooser.FILES_ONLY, "jar2dex", Suffix.JAR, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.jar2dex(path)
                }
            })
        }

        val backToCompileBtn = JButton("backToApk")
        backToCompileBtn.addActionListener {
            FileChooser.newInstance(JFileChooser.DIRECTORIES_ONLY, "backToApk", object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.backToApk(path)
                }
            })
        }
        val signBtn = JButton("sign")
        signBtn.addActionListener {
            SignDialog.showDialog()
        }
        val alignBtn = JButton("align")
        alignBtn.addActionListener {
            FileChooser.newInstance(JFileChooser.FILES_ONLY, "align apk", Suffix.APK, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.alignApk(path)
                }
            })
        }
        val generateBtn = JButton("patch")
        generateBtn.addActionListener {
            PatchDialog.showDialog()
        }

        defaultCloseOperation = EXIT_ON_CLOSE
        layout = GridLayout(0, 2)
        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        jMenuBar = createMenuBar()
        add(jadxBtn)
        add(decompileBtn)
        add(dex2jarBtn)
        add(jar2dexBtn)
        add(backToCompileBtn)
        add(alignBtn)
        add(signBtn)
        add(generateBtn)
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