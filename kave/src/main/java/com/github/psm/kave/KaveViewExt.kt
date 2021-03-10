package com.github.psm.kave

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.view.drawToBitmap
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

private const val TAG = "Kave"

/**
 * @param format inline format [Kave.Format]
 */
@RequiresPermission(allOf = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE])
fun View.saveImage(format: String? = null, callback: Kave.OnSaveCallback? = null) {
    val version = Build.VERSION.SDK_INT
    when {
        (version >= Build.VERSION_CODES.Q)  -> saveImageR(this, format, callback)
        (Build.VERSION_CODES.M..Build.VERSION_CODES.P).contains(version) -> saveImageM(this, format, callback)
    }
}

@Suppress("DEPRECATION")
private fun saveImageM(view: View, format: String?, callback: Kave.OnSaveCallback?) {
    try {
        val kavePrefUtils = KavePrefUtils(view.context)
        val rootPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + kavePrefUtils.subLocation)
                .also {
                    if (!it.exists())
                        it.mkdirs()
                }
        val fileExt = if (format != null) kavePrefUtils.getFileExts(format) else kavePrefUtils.fileExtensions
        val fileName =
            "${kavePrefUtils.fileNamePrefix}_${System.nanoTime()}.${fileExt}"
        val filePath = File(rootPath, fileName)
        val mimeType = "image/${kavePrefUtils.fileExtensions}"
        val bitmap = view.drawToBitmap()
        val compressFormat: Bitmap.CompressFormat =
            if (format != null) kavePrefUtils.getCompressFormat(format) else kavePrefUtils.compressFormat
        val ots = BufferedOutputStream(FileOutputStream(filePath))
        bitmap.compress(compressFormat, kavePrefUtils.compressValue, ots)
        ots.flush()
        ots.close()
        MediaScannerConnection.scanFile(
            view.context,
            arrayOf(filePath.absolutePath),
            arrayOf(mimeType),
            null
        )
        callback?.onSuccess(Uri.fromFile(filePath), filePath.absolutePath)
    } catch (e: Exception) {
        Log.i(TAG, "saveImage", e)
        callback?.onError(e)
    }
}

@SuppressLint("InlinedApi", "NewApi")
private fun saveImageR(view: View, format: String?, callback: Kave.OnSaveCallback?) {
    try {
        val kavePrefUtils = KavePrefUtils(view.context)
        val fileExt = if (format != null) kavePrefUtils.getFileExts(format) else kavePrefUtils.fileExtensions
        val fileName =
            "${kavePrefUtils.fileNamePrefix}_${System.nanoTime()}.${fileExt}"
        val mimeType = "image/${kavePrefUtils.fileExtensions}"
        val compressFormat: Bitmap.CompressFormat =
            if (format != null) kavePrefUtils.getCompressFormat(format) else kavePrefUtils.compressFormat
        val compressValue = kavePrefUtils.compressValue

        val contentResolver = view.context.contentResolver
        val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val bitmap = view.drawToBitmap()
        val imgOutStream: OutputStream
        val contentValue = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                Environment.DIRECTORY_PICTURES + kavePrefUtils.subLocation
            )
        }

        val uri: Uri
        contentResolver.run {
            uri = contentResolver.insert(contentUri, contentValue) ?: return
            imgOutStream = openOutputStream(uri) ?: return
        }

        imgOutStream.use { bitmap.compress(compressFormat, compressValue, it) }
        callback?.onSuccess(uri, uri.getAbsolutePath(view.context))
    } catch (e: Exception) {
        Log.e(TAG, "saveImage", e)
        callback?.onError(e)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun Uri.getAbsolutePath(context: Context): String {
    val filePathColumn =
        arrayOf(MediaStore.Images.Media.RELATIVE_PATH, MediaStore.Images.Media.DISPLAY_NAME)
    val cursor: Cursor? =
        context.contentResolver.query(this, filePathColumn, null, null, null)
    return if (cursor?.moveToFirst() == true) {
        val bucketIndex: Int = cursor.getColumnIndex(filePathColumn[0])
        val fileNameIndex: Int = cursor.getColumnIndex(filePathColumn[1])
        "${cursor.getString(bucketIndex)}/${cursor.getString(fileNameIndex)}".also {
            cursor.close()
        }
    } else {
        "".also {
            cursor?.close()
        }
    }
}