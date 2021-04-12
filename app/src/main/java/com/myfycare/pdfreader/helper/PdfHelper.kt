@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.myfycare.pdfreader.helper

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.annotation.RawRes
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import kotlin.math.absoluteValue

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

    private fun copyToLocalCacheNew(context: Context, outPutFile: File, uri: Uri) {
        if (!outPutFile.exists()) {
            val input: InputStream = context.contentResolver.openInputStream(uri)!!
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

    fun openPdfFromStorage(context: Context, uri: Uri, fileName: String){
        val fileCopy = File(context.cacheDir, fileName)
        copyToLocalCacheNew(context, fileCopy, uri)
    }

    fun openRenderer(context: Context, file: File){
        //val file = File(context.cacheDir, fileName)
        if (!file.exists()){
            val inputAsset: InputStream = context.assets.open(file.name)
            val output = FileOutputStream(file)
            val buffer = ByteArray(1024)
            var size: Int
            while (inputAsset.read(buffer).also { size = it } != -1) {
                output.write(buffer, 0, size)
            }
            inputAsset.close()
            output.close()
        }
    }

    fun pdfReaderNew(root: File): ArrayList<File> {
        val fileList: ArrayList<File> = ArrayList()
        val listAllFiles = root.listFiles()

        if (listAllFiles != null && listAllFiles.isNotEmpty()) {
            for (currentFile in listAllFiles) {
                if (currentFile.name.endsWith(".pdf") || currentFile.name.endsWith(".jpg") || currentFile.name.endsWith(".png")) {
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

    fun searchAllThePdf(file: File): ArrayList<File>{
        val pdfPattern = ".pdf"
        val fileArray: ArrayList<File> = ArrayList()
        val fileList: Array<File> = file.listFiles()

        for (i in fileList.indices) {
            if (fileList[i].isDirectory) {
                searchAllThePdf(fileList[i])
            } else {
                if (fileList[i].name.endsWith(pdfPattern)) {
                    //here you have that file.
                    Log.d("PDF_FILE", "Here is the pdf files ${fileList[i].name} ")
                    Log.d("PDF_FILE", "Here is the pdf files ${fileList[i].absolutePath} ")
                    Log.d("NEW_PDF_FILE", "Here is the pdf files ${fileList[i].absoluteFile} ")
                    Log.d("NEW_PDF_FILE_URI", "Here is the pdf files ${fileList[i].toUri()} ")
                    //val absoluteFileArray = fileList[i].absolutePath
                    fileArray.add(fileList[i].absoluteFile)
                    Log.d("FILE_ARRAY", "Array of the File are $fileArray and the size is ${fileArray.size}")
                }
            }
        }
        Log.d("FILE_ARRAY", "FILE ARRAY OUTSIDE LOOP ---> $fileArray & size is ${fileArray.size}" )
        return fileArray
    }

}