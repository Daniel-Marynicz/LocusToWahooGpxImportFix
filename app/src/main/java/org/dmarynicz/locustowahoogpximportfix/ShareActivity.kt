package org.dmarynicz.locustowahoogpximportfix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.InputStream

abstract class ShareActivity : AppCompatActivity() {

    protected var tag: String = "LocusToWahooGpxImportFix"

    protected abstract fun updateGpxFile(stream: InputStream): File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "start")
        when {

            intent?.action == Intent.ACTION_SEND -> {
                if ("application/gpx+xml" == intent.type) {

                    handleShareGpx(intent)
                }
            }
            else -> {
                Log.w(tag, "Unsupported action")
            }
        }
    }

    protected fun createGpxFile(): File
    {
        val file = File(applicationContext.filesDir, "export.gpx")
        file.deleteOnExit()

        return file
    }

    private fun handleShareGpx(intent: Intent) {
        Log.d(tag, "run handleShareGpx()")
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let { it ->
            contentResolver.openInputStream(it)?.let { stream ->
                shareGpxFile(
                    updateGpxFile(stream)
                )
            }
        }
        finishAffinity()
    }

    private fun fileToUri(file: File): Uri {
        return FileProvider.getUriForFile(
            applicationContext,
            applicationContext.packageName.toString() + ".fileProvider",
            file
        )
    }

    private fun shareGpxFile(file: File) {
        var intent = Intent(Intent.ACTION_VIEW)
        intent.setType("text/*")
        intent.data = fileToUri(file)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 0)
    }
}
