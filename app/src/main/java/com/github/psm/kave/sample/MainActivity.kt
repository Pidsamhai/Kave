package com.github.psm.kave.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.github.psm.kave.saveBitmap
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        img1.setImageResource(R.drawable.ic_dinosaur)
        btn_save.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
//            val bitmap = resources.getDrawable(R.drawable.ic_dinosaur).toBitmap()
                val bitmap =
                    ResourcesCompat.getDrawable(resources, R.drawable.ic_dinosaur, null)?.toBitmap()
                bitmap?.saveBitmap(this@MainActivity)
            }
        }

    }
}