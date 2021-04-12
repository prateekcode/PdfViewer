package com.myfycare.pdfreader.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myfycare.pdfreader.R
import com.myfycare.pdfreader.model.FileType
import kotlinx.android.synthetic.main.pdf_document_file.view.*
import java.util.*

class FileAdapter
    (
    private val fileList: ArrayList<FileType>, private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<FileAdapter.FileViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewModel {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.pdf_document_file, parent, false)
        return FileViewModel(view, onItemClickListener)
    }

    override fun onBindViewHolder(holder: FileViewModel, position: Int) {
        val files = fileList[position]
        holder.bind(files)
    }

    override fun getItemCount(): Int = fileList.size

    class FileViewModel(itemView: View, onItemClickListener: OnItemClickListener):RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                onItemClickListener.onClick(adapterPosition)
            }
        }

        fun bind(files: FileType){
            val fileThumbnail = itemView.file_icon
            Glide.with(itemView).load(files.fileThumbnail).into(fileThumbnail)
            itemView.file_name_text_view.text = files.fileTitle
        }
    }

    interface OnItemClickListener{
        fun onClick(position: Int)
    }

}