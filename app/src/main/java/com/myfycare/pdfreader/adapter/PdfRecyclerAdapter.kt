package com.myfycare.pdfreader.adapter

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.myfycare.pdfreader.ExtensionFunctions.renderAndClose
import com.myfycare.pdfreader.R

class PdfRecyclerAdapter(private val renderer: PdfRenderer, private val pageWidth: Int) :
    RecyclerView.Adapter<PdfRecyclerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(bitmap: Bitmap){
            (itemView as ImageView).setImageBitmap(bitmap)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.pdf_single_item, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(renderer.openPage(position).renderAndClose(pageWidth))
    }

    override fun getItemCount(): Int = renderer.pageCount
}
