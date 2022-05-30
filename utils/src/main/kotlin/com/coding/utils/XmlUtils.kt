package com.coding.utils

import org.w3c.dom.Document
import java.io.File
import java.io.FileInputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

object XmlUtils {

    fun newDoc(): Document {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        return builder.newDocument()
    }


    fun parse(xmlPath: String): Document? {
        if (!xmlPath.isFilePathValid(".xml")) return null
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        var doc: Document? = null
        try {
            FileInputStream(xmlPath).use {
                doc = builder.parse(xmlPath)
            }
        } catch (_: Exception) {
        }
        return doc
    }

    fun saveXml(doc: Document, outPath: String) {
        val transFactory = TransformerFactory.newInstance()
        val transformer = transFactory.newTransformer()
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes")
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0")
        val xmlSource = DOMSource(doc)
        val outputTag = StreamResult(outPath)
        val file = File(outPath)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        transformer.transform(xmlSource, outputTag)
    }
}