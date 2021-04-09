package com.myfycare.pdfreader.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.myfycare.pdfreader.R
import com.myfycare.pdfreader.helper.PdfHelper
import com.myfycare.pdfreader.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.jar.Manifest


class MainActivity : AppCompatActivity() {

    private var isGenerating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionCheck(true)

        pdf_view.setOnClickListener {
            startActivity(Intent(this, PdfViewer::class.java))
        }

        multiplePageBtn.setOnClickListener {
            startActivity(Intent(this, MultipleViewer::class.java))
        }

        val gpath: String = Environment.getExternalStorageDirectory().absolutePath
        val spath = ""
        val fullpath = File(gpath + File.separator + spath)
        PdfHelper.searchAllThePdf(fullpath)

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