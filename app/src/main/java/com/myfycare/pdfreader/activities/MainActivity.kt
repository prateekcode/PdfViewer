package com.myfycare.pdfreader.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.myfycare.pdfreader.R
import com.myfycare.pdfreader.adapter.FileAdapter
import com.myfycare.pdfreader.helper.PdfHelper
import com.myfycare.pdfreader.model.FileType
import com.myfycare.pdfreader.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.ArrayList


class MainActivity : AppCompatActivity(), FileAdapter.OnItemClickListener {

    private var isGenerating = false
    private lateinit var fileList: ArrayList<FileType>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionCheck(true)


        val pdfList = recyclerView


        val gpath: String = Environment.getExternalStorageDirectory().absolutePath
        val spath = ""
        val fullpath = File(gpath + File.separator + "/TestReport" + spath)
        PdfHelper.searchAllThePdf(fullpath)

        fileList = ArrayList<FileType>()

        Log.d("LISTS", "---------> ${PdfHelper.searchAllThePdf(fullpath)}")
        val lastPdf = PdfHelper.pdfReaderNew(fullpath)
        var imgRes: Int
        var newName: String
        var fileUri: Uri
        for (i in lastPdf) {
            Log.d("LAST_PDF", "Pdf's are ${i.name}")
            when {
                i.name.endsWith(".pdf") -> {
                    imgRes = R.drawable.ic_pdf
                    newName = i.name
                    fileUri = i.toUri()
                }
                i.name.endsWith(".png") -> {
                    imgRes = R.drawable.ic_pngfile
                    newName = i.name
                    fileUri = i.toUri()
                }
                i.name.endsWith(".jpeg") -> {
                    imgRes = R.drawable.ic_jpgfile
                    newName = i.name
                    fileUri = i.toUri()
                }
                else -> {
                    imgRes = R.drawable.ic_jpgfile
                    newName = i.name
                    fileUri = i.toUri()
                }
            }
            val fileType = FileType(imgRes, newName, fileUri)
            fileList.add(fileType)
        }

        pdfList.adapter = FileAdapter(fileList, this)
        pdfList.layoutManager = LinearLayoutManager(this)
    }

    override fun onClick(position: Int) {
        val fileLocation = fileList[position].fileTitle
        val commonUri = fileList[position].fileUri
        Log.d("URI_OBTAINED", "URI is --> $commonUri")
        when {
            fileLocation.endsWith(".pdf") -> {
                val pdfViewerIntent = Intent(this, MultipleViewer::class.java)
                pdfViewerIntent.putExtra("pdf_file_path", fileLocation)
                pdfViewerIntent.putExtra("pdf_uri", commonUri.toString())
                startActivity(pdfViewerIntent)
            }
            fileLocation.endsWith(".jpg") ||
                    fileLocation.endsWith(".png") ||
                    fileLocation.endsWith(".jpeg") ->
            {
                val imageViewerIntent = Intent(this, ImageViewer::class.java)
                imageViewerIntent.putExtra("image_name", fileLocation)
                imageViewerIntent.putExtra("image_uri", commonUri.toString())
                startActivity(imageViewerIntent)
                Log.d("IMAGE FILES", "Yayyy.. this is image file")
            }
            else -> {
                Toast.makeText(applicationContext, "File Not Supported", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Not a pdf file")
            }
        }

        Log.d("TAG", "onClick: $position and the location is $fileLocation")
    }


    private fun permissionCheck(check: Boolean) {
        val permissionHelper = PermissionHelper(
            this,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            100
        )
        permissionHelper.denied {
            if (it) {
                Log.d("Permission check", "Permission denied by system")
                Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show()
                permissionHelper.openAppDetailsActivity()
            } else {
                Toast.makeText(this, "No permission granted", Toast.LENGTH_SHORT).show()
                Log.d("Permission check", "Permission denied")
            }
        }
        permissionHelper.requestAll {
            Log.d("Permission check", "All permission granted")

            if (!isGenerating && check) {
                isGenerating = true

                val handler = Handler()
                val runnable = Runnable {
                    //to avoid multiple generation at the same time. Set isGenerating = false on some delay
                    isGenerating = false
                }
                handler.postDelayed(runnable, 2000)
                //  Toast.makeText(this, "Permission Has Been Granted", Toast.LENGTH_SHORT).show()
            }

        }
        permissionHelper.requestIndividual {
            Log.d("Permission check", "Individual Permission Granted")
        }
    }


}