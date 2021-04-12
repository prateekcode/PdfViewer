package com.myfycare.pdfreader.activities

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myfycare.pdfreader.R
import com.myfycare.pdfreader.adapter.PdfRecyclerAdapter
import com.myfycare.pdfreader.helper.PdfHelper
import kotlinx.android.synthetic.main.activity_multiple_pdf.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.NullPointerException

class MultipleViewer : AppCompatActivity() {

    private lateinit var multiplePdfRecycler: RecyclerView
    private lateinit var pdfAdapter: PdfRecyclerAdapter
    var pdfRenderer: PdfRenderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_pdf)


        //val real_file = pdfIntent.getStringExtra("pdf_file")
        //var newFile = File("")

        val pdfIntent = intent
        val file_location = pdfIntent.getStringExtra("pdf_file_path")
        val file_uri = pdfIntent.getStringExtra("pdf_uri")
        Log.d("FILE", "Getting the file location $file_location")
        Log.d("FILE_URI", "GETTING FILE URI AS --------> $file_uri")
        Log.d("FILE_URI_CHANGED", "CHANGING TO USABLE FORM--------> ${file_uri!!.toUri()}")
        //newFile = File(file_location)

        multiplePdfRecycler = rv_pdf

        val file = File(cacheDir, file_location!!)
        //PdfHelper.copyToLocalCache(applicationContext, file, R.raw.test)
        // PdfHelper.copyToLocalCache(applicationContext, file, R.raw.test)
        //PdfHelper.openRenderer(applicationContext, File(cacheDir, file_location))


        PdfHelper.openPdfFromStorage(applicationContext, file_uri.toUri(), file_location)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        try {
            Log.d("NEW FILE", "onCreate: $file")
            pdfRenderer = PdfRenderer(
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            )
            //Log.d("PDF RENDERER", "VALUE OF PDF RENDERER is ${pdfRenderer.toString()}")
        } catch (e: Exception) {
            throwExceptionForPdfRenderer(e)
        }


        pdfAdapter = pdfRenderer?.let { PdfRecyclerAdapter(it, width) }!!
        multiplePdfRecycler.adapter = pdfAdapter
        multiplePdfRecycler.layoutManager = LinearLayoutManager(this)
    }

    private fun throwExceptionForPdfRenderer(exception: Exception) {
        val message: String = when (exception) {
            is IOException -> exception.localizedMessage
            is SecurityException -> exception.localizedMessage
            is FileNotFoundException -> exception.localizedMessage
            else -> exception.localizedMessage
        }
        Log.d("PDF Renderer exception", message)
    }

}