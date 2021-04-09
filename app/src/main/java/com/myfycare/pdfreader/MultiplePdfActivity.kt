package com.myfycare.pdfreader

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.myfycare.pdfreader.adapter.PdfRecyclerAdapter
import kotlinx.android.synthetic.main.activity_multiple_pdf.*

class MultiplePdfActivity : AppCompatActivity() {

    private lateinit var multiplePdfRecycler: RecyclerView
    private lateinit var pdfAdapter: PdfRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_multiple_pdf)

        multiplePdfRecycler = multiplePdfRv
        //pdfAdapter = PdfRecyclerAdapter()
        multiplePdfRecycler.adapter = pdfAdapter

    }

    fun pdfRendererMethod(){

    }
}