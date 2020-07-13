package view.dialog

import util.CMD
import util.ToolUtil
import view.FileJfc
import java.awt.*
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:反编译界面
 */
class DecompileDialog : JDialog() {
    private fun test() {
        val kit: Toolkit = Toolkit.getDefaultToolkit() // 定义工具包
        val screenSize: Dimension = kit.screenSize // 获取屏幕的尺寸
        val screenWidth = screenSize.width / 2 // 获取屏幕的宽
        val screenHeight = screenSize.height / 2 // 获取屏幕的高
        val height = height //对象的高
        val width = width //对象的宽
        setLocation(screenWidth - width / 2, screenHeight - height / 2) //设置对象居中显示
    }

    init {
        val pane = JPanel()

        val srcPathTv = JTextField(45)
        val srcPathBtn = JButton("apk path")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "选择apk", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val outPathTv = JTextField(45)
        val outPathBtn = JButton("out path")
        outPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "选择反编译输出目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val ignoreDexCb = Checkbox("ignore dex")
        val ignoreSrcCb = Checkbox("ignore resource")
        val forceCoverCb = Checkbox("force cover")
        val compileBtn = JButton("开始")


        val logTA = JTextArea("日志")
        logTA.rows = 20
        logTA.columns = 50
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true
        compileBtn.addActionListener {
            logTA.text = ""
            val dex = if (ignoreDexCb.state) "-s" else ""
            val src = if (ignoreSrcCb.state) "-r" else ""
            val forceCover = if (forceCoverCb.state) "-f" else ""
            if (srcPathTv.text == "" || outPathTv.text == "") {
                return@addActionListener
            }
            val cmd = "java -jar ${ToolUtil.getApkTool()} d ${srcPathTv.text} $dex $src $forceCover -o ${outPathTv.text}"
            logTA.text = logTA.text + cmd + "\n"
            CMD.CMD(cmd) { msg ->
                print("onContinueResult:$msg")
                logTA.text = logTA.text + msg
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)
        pane.add(outPathBtn)
        pane.add(outPathTv)

        pane.add(ignoreDexCb)
        pane.add(ignoreSrcCb)
        pane.add(forceCoverCb)

        pane.add(compileBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "反编译"
        isVisible = true

    }
}