package org.dmarynicz.locustowahoogpximportfix

import android.util.Log
import org.w3c.dom.Document
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ShareWithRemovedAllWaypoints : ShareActivity() {

    override fun updateGpxFile(stream: InputStream): File {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document: Document = documentBuilder.parse(stream)
        var waypoints = document.getElementsByTagName("wpt")
        for (i in waypoints.length-1 downTo 0) {
            var item = waypoints.item(i)
            var node  = item?.parentNode
            if (item !== null && node !== null) {
                node.removeChild(item)
            }
        }
        waypoints = document.getElementsByTagName("wpt")
        Log.d(tag, "Waypoints left ${waypoints.length}")
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

