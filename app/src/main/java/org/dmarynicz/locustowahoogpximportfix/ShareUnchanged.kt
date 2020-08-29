package org.dmarynicz.locustowahoogpximportfix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.xml.transform.Result
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class ShareUnchanged : ShareActivity() {

    override fun updateGpxFile(stream: InputStream): File {
        val exportedFile = createGpxFile()
        val outStream = FileOutputStream(exportedFile)
        stream.copyTo(outStream)

        return exportedFile;
    }
}
