package com.coding.tool.util

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object SignCfgUtil {

    private const val SIGN_PATH = "config/sign_config.xml"
    private const val TAG_SIGN = "sign"

    init {
        initSignXml()
    }

    fun initSignXml() {
        if (!FileUtils.isFileExists(SIGN_PATH)) {
            FileUtils.copyFile(PathUtils.getSignConfigXml(), SIGN_PATH)
            addSign(SignConfig("defaultSign", PathUtils.getDefaultSignFile(), "aa887887", "stray-coding", "aa887887"))
        }
    }

    fun getSignList(): ArrayList<SignConfig> {
        val signConfigs = arrayListOf<SignConfig>()
        if (!FileUtils.isFileExists(SIGN_PATH))
            return signConfigs
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()

            val doc = builder.parse(File(SIGN_PATH))
            val signs = doc.getElementsByTagName(TAG_SIGN)
            for (i in 0 until signs.length) {
                val element = signs.item(i) as Element
                val sign = SignConfig.transform(element)
                signConfigs.add(sign)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return signConfigs
    }

    fun addSign(config: SignConfig) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(File(SIGN_PATH))
            val signList = doc.getElementsByTagName(TAG_SIGN)
            for (i in 0 until signList.length) {
                val element = signList.item(i) as Element
                if (element.getAttribute("name") == config.name) {
                    element.setAttribute("ks", config.path)
                    element.setAttribute("ks-pass", config.pwd)
                    element.setAttribute("key-alias", config.alias)
                    element.setAttribute("key-pass", config.aliasPwd)
                    saveXml(doc, SIGN_PATH)
                    return
                }
            }

            val element = doc.createElement(TAG_SIGN)
            element.setAttribute("name", config.name)
            element.setAttribute("ks", config.path)
            element.setAttribute("ks-pass", config.pwd)
            element.setAttribute("key-alias", config.alias)
            element.setAttribute("key-pass", config.aliasPwd)
            val signs = doc.getElementsByTagName("signs").item(0)
            signs.insertBefore(element, null)
            saveXml(doc, SIGN_PATH)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteSign(config: SignConfig?) {
        if (config == null) return
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(File(SIGN_PATH))
            val signList = doc.getElementsByTagName(TAG_SIGN)
            for (i in 0 until signList.length) {
                val element = signList.item(i) as Element
                if (element.getAttribute("name") == config.name) {
                    val signs = doc.getElementsByTagName("signs").item(0)
                    signs.removeChild(element)
                    saveXml(doc, SIGN_PATH)
                    return
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveXml(doc: Document, outPath: String) {
        val transFactory = TransformerFactory.newInstance()
        val transformer = transFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes")
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0")
        val xmlSource = DOMSource(doc)
        val outputTag = StreamResult(outPath)
        transformer.transform(xmlSource, outputTag)
    }

    class SignConfig(var name: String = "", var path: String = "", var pwd: String = "", var alias: String = "", var aliasPwd: String = "") {
        private constructor() : this("", "", "", "", "")

        companion object {
            fun transform(element: Element): SignConfig {
                val signConfig = SignConfig()
                try {
                    signConfig.name = element.getAttribute("name")
                    signConfig.path = element.getAttribute("ks")
                    signConfig.pwd = element.getAttribute("ks-pass")
                    signConfig.alias = element.getAttribute("key-alias")
                    signConfig.aliasPwd = element.getAttribute("key-pass")
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("sign configuration error")
                }
                return signConfig
            }
        }
    }
}