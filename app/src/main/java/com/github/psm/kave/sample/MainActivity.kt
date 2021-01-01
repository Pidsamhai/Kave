package com.github.psm.kave.sample

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.psm.kave.Kave
import com.github.psm.kave.sample.databinding.ActivityMainBinding
import com.github.psm.kave.saveImage
import java.util.*

class MainActivity : AppCompatActivity(), Kave.OnSaveCallback {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.img1.setImageResource(R.drawable.ic_dinosaur)
        binding.btnSave.setOnClickListener {
            binding.content.saveImage(callback = this)
        }
    }

    override fun onComplete(path: String) {
        binding.textView.text = path
    }

    override fun onSuccess(uri: Uri, path: String) {
        binding.textView.text = path
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show()
    }

    override fun onError(e: Exception) {
        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
    }
}