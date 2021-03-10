package com.github.psm.kave.sample

import android.app.Application
import com.github.psm.kave.Kave

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val config = Kave.Config(
            subLocation = "/SUBFOLDER/IMG",
            compressFormat = Kave.Format.JPG,
            fileNamePrefix = "MY_PREFIX",
            compressValue = 20
        )
        Kave.init(applicationContext, config)
    }
}