package com.github.psm.kave.sample

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.github.psm.kave.Kave
import com.github.psm.kave.saveBitmap
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), Kave.OnSaveCallback {
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        img1.setImageResource(R.drawable.ic_dinosaur)
        btn_save.setOnClickListener {
            val bitmap =
                ResourcesCompat.getDrawable(resources, R.drawable.ic_dinosaur, null)?.toBitmap()
            bitmap?.saveBitmap(
                this@MainActivity,
                callback = this@MainActivity,
                fileName = "PreFix${Date().time}",
                subLocation = "Invoice",
                compressFormat = Bitmap.CompressFormat.WEBP
            )
        }

    }

    override fun onComplete(path: String) {
        textView.text = path
    }

    override fun onError(e: Exception) {
        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
    }
}