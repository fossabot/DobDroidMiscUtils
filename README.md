# DobDroidMiscUtils

### A small and useful set of Android utility classes written in kotlin to reduce boilerplate

### Import

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
```
dependencies {
    implementation 'com.github.andob:DobDroidMiscUtils:v1.1.2'
}
```

### Table of Contents

1. [ToolbarX](#toolbarx)
2. [BottomNavigationViewX](#bottomviewnavigationx)
3. [ViewPagerX](#viewpager)
4. [EditTextX](#edittext)
5. [ScreenSize](#screensize)
6. [PermissionsHandler](#permissions)
7. [Keyboard](#keyboard)
8. [FileManager](#filemanager)
9. [RetrofitX](#retrofit)
10. [CacheDelegate](#cache)
11. [Yield](#yield)
12. [Library dependencies](#dependencies)

#### ToolbarX <a name="toolbarx"></a>

Toolbar eXtension methods. To setup a toolbar with back button:

```kotlin
toolbar.setupBackIcon()
```

Toolbar with menu on the right:

```kotlin
toolbar.setMenu(R.menu.menu_main)
toolbar[R.id.add] = {
    Log.e("a", "add logic")
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:tools="http://schemas.android.com/tools"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      tools:context=".MainActivity">
    <item
        android:id="@+id/add"
        android:title="Add"
        app:showAsAction="always" />
</menu>
```

If you are using menu or back button, override:

```kotlin
override fun onCreateOptionsMenu(menu: Menu?): Boolean
{
    toolbar.onCreateOptionsMenu(menuInflater, menu)
    return super.onCreateOptionsMenu(menu)
}
```

If you are using menu, override:

```kotlin
override fun onOptionsItemSelected(item: MenuItem?) : Boolean
{
    toolbar.onOptionsItemSelected(item)
    return super.onOptionsItemSelected(item)
}
```

Toolbar with hamburger menu icon: Please import [this](https://github.com/balysv/material-menu) library.

```
implementation 'com.balysv.materialmenu:material-menu:2.0.0'
``` 

```kotlin
toolbar.setupHamburgerMenu()
toolbar.setOnHamburgerMenuClickedListener {
    Log.e("a", "toggle drawer")
}
```

#### BottomNavigationViewX <a name="bottomviewnavigationx"></a>

BottomNavigationView eXtension method

```kotlin
bottomNavigationView.setupWithViewPager(viewPager)
```

```kotlin
bottomNavigationView.setupWithViewPager(viewPager, initialTab = 0)
```

#### ViewPagerX <a name="viewpager"></a>

ViewPager eXtension method

```kotlin
viewPager.setOnPageChangedListener { page ->
    Log.e("a", "You're on page $page")
}
```

#### EditTextX <a name="edittext"></a>

EditText eXtension methods

```kotlin
editText.setOnTextChangedListener { newText ->
    Log.e("a", newText)
}
```

```kotlin
editText.setOnEditorActionListener { actionId ->
    if (actionId==EditorInfo.IME_ACTION_DONE)
        Log.e("a", "enter pressed")
}
```

#### ScreenSize <a name="screensize"></a>

Used to get the width, height and density of the screen. In Application Class ``onCreate`` method:

```kotlin
ScreenSize.init(withContext = this)
```

Then, you can simply use the properties from ``ScreenSize``:

```kotlin
if (ScreenSize.width<ScreenSize.height)
    if (ScreenSize.density>=2.0) {...code...}
```

#### PermissionsHandler <a name="permissions"></a>

Used to ask for dangerous permisssions. In the Activity class:

```kotlin
class MainActivity : AppCompatActivity()
{
    val permissionsHandler = PermissionsHandler()
    
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
```

To ask for permissions, simply use:

```kotlin
permissionsHandler.askFor(arrayOf(Manifest.permission.CAMERA))
    .onGranted { Log.e("a", "YAY") }
    .onDenied { Log.e("a", "NAY") }
    .withContext(context = this)
```

#### Keyboard <a name="keyboard"></a>

Used to open or close the keyboard

```kotlin
Keyboard.open(on = context)
```

```kotlin
Keyboard.close(on = context)
```

#### FileManager <a name="filemanager"></a>

Extend ``FileManager`` class to create classes that manages app file's paths. For instance, ``getSampleFilePath`` will return ``/storage/emulated/0/Dob/sample.pdf``

```kotlin
object AppFileManager : FileManager()
{
    override fun folderBasePath() : String = Environment.getExternalStorageDirectory().absolutePath
    override fun folderName() : String = "Dob"

    fun getSampleFilePath() : String = getFilePath("sample.pdf")
}
```

#### Retrofit extensions <a name="retrofit"></a>

Download and upload files:

```kotlin
interface IApiClient
{
    @Streaming
    @GET("/samples/pdf.pdf")
    fun downloadPdf() : Call<ResponseBody>

    @Multipart
    @POST("/samples/pdf.pdf")
    fun uploadPdf(@Part file : MultipartBody.Part) : Call<Unit?>
}
```

To download a file:

```kotlin
ApiClient.Instance.downloadPdf().enqueue(object : Callback<ResponseBody>
{
    override fun onFailure(call: Call<ResponseBody>, t: Throwable)
    {
        Log.e("a", "fail")
    }

    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
    {
        Thread {
            try
            {
                downloadFile(response.body()!!, outputPath = AppFileManager.getSampleFilePath())
                Log.e("a", "success")
            }
            catch (ex : Exception)
            {
                Log.e("a", "fail")
            }
        }.start()
    }
})
```

To upload a file:

```kotlin
ApiClient.Instance.uploadPdf(fileUpload(context = this, path = AppFileManager.getSampleFilePath()))
    .enqueue(object : Callback<Unit?>
    {
        override fun onFailure(call: Call<Unit?>, t: Throwable)
        {
            Log.e("a", "fail")
        }

        override fun onResponse(call: Call<Unit?>, response: Response<Unit?>)
        {
            Log.e("a", "success")
        }
    })
```

#### Cache delegate <a name="cache"></a>

Use ``by cache()`` delegate to cache in memory values returned by other delegates. For instance, with kotpref library:

```kotlin
object Preferences : KotprefModel()
{
    var username by cached(stringPref())
    var level by cached(intPref())
    var firstRun by booleanPref(default = true)
}
```

Username will be returned from memory cache. If not found in cache, it will be fetched by ``stringPref`` delegate, from ``SharedPreferences``.

#### Yield expressions <a name="yield"></a>

Similar to C# / Scala ``yield`` keyword:

```kotlin
fun findFilesIn(directory : File) : List<File>
{
    val files=mutableListOf<File>()
    directory.listFiles()?.forEach { file ->
        if (file.isDirectory)
            files.addAll(findFilesIn(directory = file))
        else files.add(file)
    }

    return files
}
```

...is equivalent to

```kotlin
fun findFilesIn(directory : File) : List<File> = yieldListOf<File> {
    directory.listFiles()?.forEach { file ->
        if (file.isDirectory)
            yield(findFilesIn(directory = file))
        else yield(file)
    }
}
```

#### Library dependencies <a name="dependencies"></a>

This library depends on the following libraries:

```
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'com.android.support:design:28.0.0'
implementation 'com.android.support:recyclerview-v7:28.0.0'
implementation 'com.balysv.materialmenu:material-menu:2.0.0'
implementation 'com.squareup.okhttp3:okhttp:3.12.0'
```

You can exclude any of those:

```
implementation ('com.github.andob:DobDroidMiscUtils:v1.0.7') {
    exclude group: 'com.android.support'
    exclude group: 'com.balysv.materialmenu'
    exclude group: 'com.squareup.okhttp3'
}
```

### License

```java
Copyright 2019 Andrei Dobrescu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.`
```
