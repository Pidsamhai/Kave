package com.github.psm.kave

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*


private const val TAG = "Kave"

/**
 * @param context
 * @param location
 * Example [Environment.DIRECTORY_DCIM] for DCIM [Environment.DIRECTORY_PICTURES] for Pictures
 * @param fileName for filename
 * @param compressFormat default PNG [Bitmap.CompressFormat.PNG]
 */
fun Bitmap.saveBitmap(
    context: Context,
    location: String = Environment.DIRECTORY_PICTURES,
    subLocation: String = "",
    fileName: String = Date().time.toString(),
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG
) {
    when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.Q -> saveBitmapQ(
            context,
            location,
            subLocation,
            fileName,
            compressFormat
        )
        in Build.VERSION_CODES.M..Build.VERSION_CODES.P -> saveBitmapM(
            context,
            location,
            subLocation,
            fileName,
            compressFormat
        )
    }
}

/**
 * Q
 */
private fun Bitmap.saveBitmapQ(
    context: Context,
    location: String,
    subLocation: String,
    fileName: String,
    compressFormat: Bitmap.CompressFormat
) {
    var fos: OutputStream? = null
    try {
        val filename = "$fileName.png"
        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            "$location/$subLocation"
        )
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = resolver.openOutputStream(imageUri!!)
        this.compress(compressFormat, 100, fos)
        fos?.close()
    } catch (e: Exception) {
        Log.i(TAG, "saveBitmapQ", e)
    }
}

/**
 * M
 */
private fun Bitmap.saveBitmapM(
    context: Context,
    location: String,
    subLocation: String,
    fileName: String,
    compressFormat: Bitmap.CompressFormat
) {
    Log.i(TAG, "saveBitmapM: ")
    try {
        val rootPath = Environment.getExternalStoragePublicDirectory("$location/$subLocation").also {
            if(!it.exists())
                it.mkdir()
        }
        val filePath = File(rootPath, "$fileName.png")
        val ots = BufferedOutputStream(FileOutputStream(filePath))
        this.compress(compressFormat, 80, ots)
        ots.flush()
        ots.close()
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath.absolutePath),
            arrayOf("image/jpg"),
            null
        );
    } catch (e: Exception) {
        Log.i(TAG, "saveBitmapM", e)
    }
}