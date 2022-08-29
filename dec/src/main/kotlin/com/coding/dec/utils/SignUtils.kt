package com.coding.dec.utils

import com.coding.utils.FileUtils
import com.coding.utils.XmlUtils
import com.coding.utils.XmlUtils.newDoc
import org.w3c.dom.Element
import java.io.File

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object SignUtils {

    private const val SIGN_CONFIG_PATH = "config/sign_config.xml"
    private const val ELEMENT_SIGNS = "signs"
    private const val ELEMENT_SIGN = "sign"

    //签名相关
    private const val SIGN_NAME = "name"
    private const val SIGN_PATH = "path"
    private const val SIGN_PWD = "pwd"
    private const val SIGN_ALIAS = "alias"
    private const val SIGN_ALIAS_PWD = "alias_pwd"

    init {
        initSignXml()
    }

    private fun initSignXml() {
        if (!FileUtils.isFileExists(SIGN_CONFIG_PATH)) {
            try {
                val doc = newDoc()
                val root = doc.createElement(ELEMENT_SIGNS)
                val adtElement = doc.createElement(ELEMENT_SIGN)
                adtElement.setAttribute(SIGN_NAME, "adt")
                adtElement.setAttribute(SIGN_PATH, Tools.getDefaultSignFile())
                adtElement.setAttribute(SIGN_PWD, "adt123")
                adtElement.setAttribute(SIGN_ALIAS, "adt")
                adtElement.setAttribute(SIGN_ALIAS_PWD, "adt123")
                root.appendChild(adtElement)
                doc.appendChild(root)
                XmlUtils.saveXml(doc, File(SIGN_CONFIG_PATH).absolutePath)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSignList(): ArrayList<SignBean> {
        val signArray = arrayListOf<SignBean>()
        if (!FileUtils.isFileExists(SIGN_CONFIG_PATH)) return signArray
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            val root = doc!!.documentElement
            val signList = doc.getElementsByTagName(ELEMENT_SIGN)
            for (i in 0 until signList.length) {
                val item = signList.item(i) as Element
                SignBean.transform(item)?.let {
                    val file = File(it.path)
                    //不存在就删除
                    if (!file.exists()) {
                        root.removeChild(item)
                        return@let
                    }
                    signArray.add(it)
                }
            }
            XmlUtils.saveXml(doc, SIGN_CONFIG_PATH)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return signArray
    }

    /**
     * 根据name获取签名
     *
     * @param name  签名name
     * @return
     */
    fun getSign(name: String): SignBean {
        for (item in getSignList()) {
            if (name == item.name) {
                return item
            }
        }
        return SignBean()
    }

    /**
     * 添加签名到xml中
     *
     * @param config
     */
    fun addSign(config: SignBean) {
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            doc?.let {
                val element = doc.createElement(ELEMENT_SIGN)
                element.setAttribute(SIGN_NAME, config.name)
                element.setAttribute(SIGN_PATH, config.path)
                element.setAttribute(SIGN_PWD, config.pwd)
                element.setAttribute(SIGN_ALIAS, config.alias)
                element.setAttribute(SIGN_ALIAS_PWD, config.aliasPwd)
                //添加节点
                val root = doc.documentElement
                root.insertBefore(element, null)
                XmlUtils.saveXml(doc, SIGN_CONFIG_PATH)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 从xml中删除签名
     *
     * @param config
     */
    fun deleteSign(config: SignBean?) {
        if (config == null) return
        try {
            val doc = XmlUtils.parse(SIGN_CONFIG_PATH)
            doc?.let {
                val signList = doc.getElementsByTagName(ELEMENT_SIGN)
                for (i in 0 until signList.length) {
                    val element = signList.item(i) as Element
                    if (element.getAttribute("name") == config.name) {
                        val root = doc.documentElement
                        root.removeChild(element)
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

fun main() {
    val doc = XmlUtils.parse("config/sign_config.xml")
    println(doc!!.documentElement.tagName)
}