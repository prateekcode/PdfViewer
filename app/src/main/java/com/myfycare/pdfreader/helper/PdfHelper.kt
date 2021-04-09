package com.myfycare.pdfreader.helper

import android.content.Context
import androidx.annotation.RawRes
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object PdfHelper {

    private fun copyToLocalCache(context: Context,outPutFile: File, @RawRes pdfResource: Int) {
        if (!outPutFile.exists()) {
            val input: InputStream = context.resources.openRawResource(pdfResource)
            val output = FileOutputStream(outPutFile)
            val buffer = ByteArray(1024)
            var size: Int
            while (input.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            input.close()
            output.close()
        }
    }

}