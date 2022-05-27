package com.coding.dec.utils

import com.coding.utils.FileUtils
import com.coding.utils.XmlUtils
import com.coding.utils.XmlUtils.newDoc
import org.w3c.dom.Element

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object SignUtils {

    private const val SIGN_CONFIG_PATH = "config/sign_config.xml"
    private const val TAG_SIGNS = "signs"
    private const val TAG_SIGN = "sign"

    //签名相关
    private const val SIGN_NAME = "name"
    private const val SIGN_PATH = "path"
    private const val SIGN_PWD = "pwd"
    private const val SIGN_ALIAS = "alias"
    private const val SIGN_ALIAS_PWD = "alias_pwd"

    init {
        initSignXml()
    }

    fun initSignXml() {
        if (!FileUtils.isFileExists(SIGN_CONFIG_PATH)) {
            try {
                val doc = newDoc()
                val root = doc.createElement(TAG_SIGNS)
                val default = doc.createElement("sign")
                default.setAttribute(SIGN_NAME, "adt")
                default.setAttribute(SIGN_PATH, Tools.getDefaultSignFile())
                default.setAttribute(SIGN_PWD, "adt123")
                default.setAttribute(SIGN_ALIAS, "adt")
                default.setAttribute(SIGN_ALIAS_PWD, "adt123")
                root.appendChild(default)
                doc.appendChild(root)
                XmlUtils.saveXml(doc, SIGN_CONFIG_PATH)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //获取签名列表
    fun getSignList(): ArrayList<SignBean> {
        val signArray = arrayListOf<SignBean>()
        if (!FileUtils.isFileExists(SIGN_CONFIG_PATH))
            return signArray
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            val signs = doc!!.getElementsByTagName(TAG_SIGN)
            for (i in 0 until signs.length) {
                val element = signs.item(i) as Element
                SignBean.transform(element)?.let {
                    signArray.add(it)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return signArray
    }

    fun addSign(config: SignBean) {
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            doc?.let {
                val node = doc.createElement(TAG_SIGN)
                node.setAttribute(SIGN_NAME, config.name)
                node.setAttribute(SIGN_PATH, config.path)
                node.setAttribute(SIGN_PWD, config.pwd)
                node.setAttribute(SIGN_ALIAS, config.alias)
                node.setAttribute(SIGN_ALIAS_PWD, config.aliasPwd)
                val signs = doc.getElementsByTagName(TAG_SIGNS).item(0)
                signs.insertBefore(node, null)
                XmlUtils.saveXml(doc, SIGN_CONFIG_PATH)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteSign(config: SignBean?) {
        if (config == null) return
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            doc?.let {
                val signList = doc.getElementsByTagName(TAG_SIGN)
                for (i in 0 until signList.length) {
                    val element = signList.item(i) as Element
                    if (element.getAttribute("name") == config.name) {
                        val signs = doc.getElementsByTagName(TAG_SIGNS).item(0)
                        signs.removeChild(element)
                        XmlUtils.saveXml(doc, SIGN_CONFIG_PATH)
                        return
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    class SignBean(
        var name: String = "",
        var path: String = "",
        var pwd: String = "",
        var alias: String = "",
        var aliasPwd: String = ""
    ) {
        private constructor() : this("", "", "", "", "")

        companion object {
            fun transform(element: Element): SignBean? {
                try {
                    val signBean = SignBean()
                    signBean.name = element.getAttribute(SIGN_NAME)
                    signBean.path = element.getAttribute(SIGN_PATH)
                    signBean.pwd = element.getAttribute(SIGN_PWD)
                    signBean.alias = element.getAttribute(SIGN_ALIAS)
                    signBean.aliasPwd = element.getAttribute(SIGN_ALIAS_PWD)
                    return signBean
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("sign configuration error")
                }
                return null
            }
        }
    }
}