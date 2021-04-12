package com.myfycare.pdfreader.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.myfycare.pdfreader.R
import kotlinx.android.synthetic.main.activity_image_viewer.*
import java.io.IOException

class ImageViewer : AppCompatActivity() {

    private lateinit var mImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_viewer)

        mImageView = imageView
        val imageViewIntent = intent
        val image_title = imageViewIntent.getStringExtra("image_name")
        val image_uri = imageViewIntent.getStringExtra("image_uri")

        val imageUri = image_uri!!.toUri()

        supportActionBar!!.title = image_title
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        try {
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            mImageView.setImageBitmap(bitmap)
        }catch (e: IOException){
            e.printStackTrace()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }





}