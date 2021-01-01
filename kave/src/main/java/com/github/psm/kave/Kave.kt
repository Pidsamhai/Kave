package com.github.psm.kave

import android.Manifest
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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import android.view.View


private const val TAG = "Kave"

/**
 * @param context
 * @param location
 * Example [Environment.DIRECTORY_DCIM] for DCIM [Environment.DIRECTORY_PICTURES] for Pictures
 * @param fileName for filename
 * @param compressFormat default PNG [Bitmap.CompressFormat.PNG]
 * @param quality bitmap compress quality default 80
 * @param callback [Kave.OnSaveCallback]
 * @deprecated use [View.saveImage]
 */
@Deprecated("migrate to View.saveImage()")
@RequiresPermission(allOf = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun Bitmap.saveBitmap(
    context: Context,
    location: String = Environment.DIRECTORY_PICTURES,
    subLocation: String = "",
    fileName: String = Date().time.toString(),
    compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 80,
    callback: Kave.OnSaveCallback? = null
) {
    when (Build.VERSION.SDK_INT) {
        Build.VERSION_CODES.Q -> saveBitmapQ(
            context,
            location,
            subLocation,
            fileName,
            compressFormat,
            quality,
            callback
        )
        in Build.VERSION_CODES.M..Build.VERSION_CODES.P -> saveBitmapM(
            context,
            location,
            subLocation,
            fileName,
            compressFormat,
            quality,
            callback
        )
        else -> Toast.makeText(context, "Not Supported", Toast.LENGTH_LONG).show()
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
    compressFormat: Bitmap.CompressFormat,
    quality: Int,
    callback: Kave.OnSaveCallback?
) {
    var fos: OutputStream? = null
    try {
        val fileName = "$fileName.${getType(compressFormat)}"
        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues()
        val path = if (subLocation.isNotEmpty()) "$location/$subLocation/" else "$location/"
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH, path
        )
        val imageUri: Uri? =
            resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = resolver.openOutputStream(imageUri!!)
        this.compress(compressFormat, quality, fos)
        fos?.close()
        val s =
            callback?.onComplete(path + fileName)
    } catch (e: Exception) {
        Log.i(TAG, "saveBitmapQ", e)
        callback?.onError(e)
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
    compressFormat: Bitmap.CompressFormat,
    quality: Int,
    callback: Kave.OnSaveCallback?
) {
    Log.i(TAG, "saveBitmapM: ")
    try {
        val rootPath =
            Environment.getExternalStoragePublicDirectory("$location/$subLocation").also {
                if (!it.exists())
                    it.mkdir()
            }
        val filePath = File(rootPath, "$fileName.${getType(compressFormat)}")
        val ots = BufferedOutputStream(FileOutputStream(filePath))
        this.compress(compressFormat, quality, ots)
        ots.flush()
        ots.close()
        MediaScannerConnection.scanFile(
            context,
            arrayOf(filePath.absolutePath),
            arrayOf("image/jpg"),
            null
        )
        callback?.onComplete(filePath.absolutePath)
    } catch (e: Exception) {
        Log.i(TAG, "saveBitmapM", e)
        callback?.onError(e)
    }
}

private fun getType(format: Bitmap.CompressFormat): String {
    return when (format) {
        Bitmap.CompressFormat.JPEG -> "jpg"
        Bitmap.CompressFormat.PNG -> "png"
        Bitmap.CompressFormat.WEBP -> "webp"
        else -> ""
    }
}

class Kave {

    interface OnSaveCallback {
        fun onComplete(path: String)
        fun onSuccess(uri: Uri, path: String) = Unit
        fun onError(e: Exception)
    }

    /**
     * @param subLocation Default Pictures folder
     * @param compressFormat [Format] default [Format.PNG]
     * @param compressValue value of compress 0 - 100 default 100
     * @param fileNamePrefix file name prefix default IMG_XXXXX
     */
    data class Config(
        val subLocation: String? = null,
        val compressFormat: String = Format.PNG,
        val compressValue: Int = 100,
        val fileNamePrefix: String? = null
    )

    object Format {
        const val PNG = "png"
        const val JPG = "jpg"
        const val WEBP = "webp"
        @RequiresApi(Build.VERSION_CODES.R)
        const val WEBP_LOSSLESS = "webp_lossless"
        @RequiresApi(Build.VERSION_CODES.R)
        const val WEBP_LOSSY = "webp_lossy"
    }

    companion object {
        fun init(context: Context, config: Config) = KavePrefUtils.init(context, config)
    }
}