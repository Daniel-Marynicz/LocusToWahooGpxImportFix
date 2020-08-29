package org.dmarynicz.locustowahoogpximportfix

import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ShareWithUpdatedAllWaypoints : ShareActivity() {

    override fun updateGpxFile(stream: InputStream): File {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document: Document = documentBuilder.parse(stream)
        val waypoints = document.getElementsByTagName("wpt")

        for (i in 0 until waypoints.length) {
            val wptNode = waypoints.item(i)
            val wptElement = wptNode as Element
            val wptName = wptElement.getElementsByTagName("name").item(0)
            val wptSym = wptElement.getElementsByTagName("sym").item(0)
            val wptDesc = document.createElement("desc")
            wptDesc.textContent = wptName.textContent
            waypoints.item(i).appendChild(wptDesc)
            when (wptSym.textContent) {
                "right_sharp" ->    {
                    wptName.textContent = "right"
                }
                "left_sharp" ->    {
                    wptName.textContent = "left"
                }
                "right" ->    {
                    wptName.textContent = "right"
                }
                "left" ->    {
                    wptName.textContent = "left"
                }
                "straight" ->  {
                    wptName.textContent = "straight"
                }
                "roundabout_e1" -> {
                    wptName.textContent = "right"
                }
                "roundabout_e2" -> {
                    wptName.textContent = "straight"
                }
                "roundabout_e3" -> {
                    wptName.textContent = "left"
                }
                else -> {
                    wptName.textContent = wptSym.textContent
                }
            }
        }

        val exportedFile = createGpxFile()
        transform(DOMSource(document), StreamResult(exportedFile))

        return exportedFile
    }

    private fun transform(xmlSource: Source, outputTarget: Result): Unit
    {
        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        transformer.transform(xmlSource, outputTarget)
    }
}