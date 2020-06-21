<h1 align="center">Kave</h1>

<p align="center">
<a href="https://jitpack.io/#Pidsamhai/Kave"> <img src="https://jitpack.io/v/Pidsamhai/Kave.svg" /></a>
<a href="https://kotlinlang.org">&nbsp<img src="https://img.shields.io/badge/Kotlin-1.3.72-blue.svg" /> </a>
<a href="https://github.com/Pidsamhai/Kave/blob/master/License.txt">&nbsp<img alt="GitHub" src="https://img.shields.io/github/license/Pidsamhai/Kave"></a>
</p>

### Simple Bitmap extension

#### Save Bitmap to storage

```kotlin
    bitmap.saveBitmap(this)
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

### Changelog

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
