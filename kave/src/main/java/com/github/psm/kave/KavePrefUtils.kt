package com.github.psm.kave

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.core.content.edit
import com.github.psm.kave.Kave.Format.JPG
import com.github.psm.kave.Kave.Format.PNG
import com.github.psm.kave.Kave.Format.WEBP
import com.github.psm.kave.Kave.Format.WEBP_LOSSLESS
import com.github.psm.kave.Kave.Format.WEBP_LOSSY

class KavePrefUtils(private val context: Context) {

    private val pref = context.getSharedPreferences(KAVE_SETTING, Context.MODE_PRIVATE)

    val subLocation: String
        get() = pref.getString(SUB_LOCATION_KEY, context.applicationInfo.name) ?: ""

    val compressFormat
        get() = getCompressFormat(pref.getString(COMPRESS_FORMAT_KEY, PNG)!!)

    val fileNamePrefix: String
        get() = pref.getString(FILE_NAME_PREFIX_KEY, DEFAULT_NAME_PREFIX)!!

    val fileExtensions: String
        get() = getFileExts(pref.getString(COMPRESS_FORMAT_KEY, PNG))

    val compressValue:Int = pref.getInt(COMPRESS_VALUE_KEY, DEFAULT_COMPRESS_VALUE)

    @SuppressLint("NewApi")
    fun getCompressFormat(type: String): Bitmap.CompressFormat {
        return when (type) {
            JPG -> Bitmap.CompressFormat.JPEG
            PNG -> Bitmap.CompressFormat.PNG
            WEBP -> Bitmap.CompressFormat.WEBP
            WEBP_LOSSLESS -> Bitmap.CompressFormat.WEBP_LOSSLESS
            WEBP_LOSSY -> Bitmap.CompressFormat.WEBP_LOSSY
            else -> throw IllegalArgumentException("Invalid CompressFormat")
        }
    }

    @SuppressLint("NewApi")
    fun getFileExts(type: String?): String {
        return when {
            type == JPG -> "jpg"
            type == PNG -> "png"
            listOf(WEBP, WEBP_LOSSY, WEBP_LOSSLESS).contains(type) -> "webp"
            else -> throw Exception("Can not get file extensions")
        }
    }

    companion object {
        private const val KAVE_SETTING = "kave_setting"
        private const val SUB_LOCATION_KEY = "location"
        private const val COMPRESS_FORMAT_KEY = "compress_format"
        private const val COMPRESS_VALUE_KEY = "compress_value"
        private const val FILE_NAME_PREFIX_KEY = "name_prefix"
        private const val DEFAULT_NAME_PREFIX = "IMG"
        private const val DEFAULT_COMPRESS_VALUE = 100

        fun init(context: Context, config: Kave.Config) {
            context.getSharedPreferences(KAVE_SETTING, Context.MODE_PRIVATE).edit(commit = true) {
                putString(SUB_LOCATION_KEY, config.subLocation ?: "")
                putString(COMPRESS_FORMAT_KEY, config.compressFormat)
                putString(FILE_NAME_PREFIX_KEY, config.fileNamePrefix)
                putInt(COMPRESS_VALUE_KEY, config.compressValue)
            }
        }
    }
}