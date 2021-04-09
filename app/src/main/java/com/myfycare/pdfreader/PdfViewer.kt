package com.myfycare.pdfreader

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RawRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pdf_viewer.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class PdfViewer : AppCompatActivity() {

    companion object {
        const val FILE_NAME = "test.pdf"
        const val TAG = "PDF_VIEWER"
    }

    private var mPdfRenderer: PdfRenderer? = null
    private lateinit var mPdfPage: PdfRenderer.Page
    private lateinit var mImageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_viewer)

        mImageView = pdfViewIV
        try {
            pdfViewerWithAndroid(this.mImageView)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    @Throws(IOException::class)
    private fun pdfViewerWithAndroid(imageView: ImageView) {
        val file = File(cacheDir, FILE_NAME)
        copyToLocalCache(file, R.raw.test)
        val fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        mPdfRenderer = PdfRenderer(fileDescriptor)
        val noOfPages = mPdfRenderer!!.pageCount
        Log.d(TAG, "No. Of Pages in the Pdf are $noOfPages")
        //mPdfPage = mPdfRenderer!!.openPage(1)
        for (i in 0 until noOfPages){
            mPdfPage = mPdfRenderer!!.openPage(i)
            //Bitmap to show the pdf file into the image view
            val bitmap = Bitmap.createBitmap(
                mPdfPage.width,
                mPdfPage.height,
                Bitmap.Config.ARGB_8888
            )
            mPdfPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

            // Set the bitmap in the ImageView so we can view it
            imageView.setImageBitmap(bitmap)

            mPdfPage.close()
        }

        mPdfRenderer!!.close()


    }

    private fun copyToLocalCache(outPutFile: File, @RawRes pdfResource: Int) {
        if (!outPutFile.exists()) {
            val input: InputStream = resources.openRawResource(pdfResource)
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