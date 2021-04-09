@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.myfycare.pdfreader.helper

import android.content.Context
import android.util.Log
import androidx.annotation.RawRes
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object PdfHelper {

    fun copyToLocalCache(context: Context, outPutFile: File, @RawRes pdfResource: Int) {
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

    fun pdfReaderNew(root: File): ArrayList<File> {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".pdf")) {
                    // File absolute path
                    Log.e("downloadFilePath", currentFile.absolutePath)
                    // File Name
                    Log.e("downloadFileName", currentFile.name)
                    fileList.add(currentFile.absoluteFile)
                }
            }
            Log.w("fileList", "" + fileList.size)
        }
        return fileList
    }

    fun searchAllThePdf(file: File){
        val pdfPattern = ".pdf"
        val fileList: Array<File> = file.listFiles()

        for (i in fileList.indices) {
            if (fileList[i].isDirectory) {
                searchAllThePdf(fileList[i])
            } else {
                if (fileList[i].name.endsWith(pdfPattern)) {
                    //here you have that file.
                    Log.d("PDF_FILE", "Here is the pdf files $fileList ")
                }
            }
        }
    }

}