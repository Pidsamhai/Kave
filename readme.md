<h1 align="center">Kave</h1>

<p align="center">
<a href="https://jitpack.io/#Pidsamhai/Kave"> <img src="https://jitpack.io/v/Pidsamhai/Kave.svg" /></a>
<a href="https://kotlinlang.org">&nbsp<img src="https://img.shields.io/badge/Kotlin-1.3.72-blue.svg" /> </a>
<a href="https://github.com/Pidsamhai/Kave/blob/master/License.txt">&nbsp<img alt="GitHub" src="https://img.shields.io/github/license/Pidsamhai/Kave"></a>
</p>

### Simple Bitmap extension

#### Save View to storage

* init 
* [Kave.Config](kave/src/main/java/com/github/psm/kave/Kave.kt)

```kotlin
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
```

* use 

```kotlin
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
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show()
    }

    override fun onError(e: Exception) {
        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
    }
}
```

* custom Image Type

```kotlin
binding.content.saveImage(format = Kave.Format.PNG)
```

## Jitpack

```text
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
## Setup

Permission AndroidManifest.xml

```xml
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```

```text
dependencies {
  implementation 'com.github.Pidsamhai:Kave:<latest-version>'
}
```

#### Support API >= 23

#### Reference

* [Android 11 Scoped Storage - Saving Files To Shared Storage](https://androidexplained.github.io/android/android11/scoped-storage/2020/09/29/file-saving-android-11.html)

### Changelog

#### 0.0.1

* android 11 (R) support
* deprecate saveBitmap
* upgrade dependencies

####  0.0.1-alpha

*  initial release

### License
```text
            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
                    Version 2, December 2004

 Copyright (C) 2020 Pidsamhai <meng348@gmail.com>

 Everyone is permitted to copy and distribute verbatim or modified
 copies of this license document, and changing it is allowed as long
 as the name is changed.

            DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
   TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

  0. You just DO WHAT THE FUCK YOU WANT TO.
```
