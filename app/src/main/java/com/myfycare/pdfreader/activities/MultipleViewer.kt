package com.myfycare.pdfreader.activities

import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myfycare.pdfreader.R
import com.myfycare.pdfreader.adapter.PdfRecyclerAdapter
import com.myfycare.pdfreader.helper.PdfHelper
import kotlinx.android.synthetic.main.activity_multiple_pdf.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MultipleViewer : AppCompatActivity() {

    private lateinit var multiplePdfRecycler: RecyclerView
    private lateinit var pdfAdapter: PdfRecyclerAdapter
    var pdfRenderer: PdfRenderer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multiple_pdf)

        multiplePdfRecycler = rv_pdf
        val file = File(cacheDir, PdfViewer.FILE_NAME)
        PdfHelper.copyToLocalCache(applicationContext, file, R.raw.test)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels

        try {
            pdfRenderer = PdfRenderer(
                ParcelFileDescriptor.open(
                    file,
                    ParcelFileDescriptor.MODE_READ_ONLY
                )
            )
        }catch (e: Exception){
            throwExceptionForPdfRenderer(e)
        }


        pdfAdapter = PdfRecyclerAdapter(pdfRenderer!!, width)
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