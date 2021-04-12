package com.myfycare.pdfreader.model

import android.net.Uri

data class FileType(
    val fileThumbnail: Int,
    val fileTitle: String,
    val fileUri: Uri
)
