# DobDroidMiscUtils
[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fandob%2FDobDroidMiscUtils.svg?type=shield)](https://app.fossa.io/projects/git%2Bgithub.com%2Fandob%2FDobDroidMiscUtils?ref=badge_shield)


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
    implementation 'com.github.andob:DobDroidMiscUtils:v1.2.1'
}
```

### Table of Contents

1. [ToolbarX](#toolbarx)
2. [BottomNavigationViewX](#bottomviewnavigationx)
3. [ViewPagerX](#viewpager)
4. [EditTextX](#edittext)
5. [ViewX](#viewx)
6. [ScreenSize](#screensize)
7. [PermissionsHandler](#permissions)
8. [Keyboard](#keyboard)
9. [FileManager](#filemanager)
10. [Color](#color)
11. [RetrofitX](#retrofit)
12. [JodaTimeX](#jodatimex)
13. [CacheDelegate](#cache)
14. [Yield](#yield)
15. [CollectionX](#collection)
16. [TypedArrayX](#typedarray)
17. [StringJsonAdapter (Gson)](#stringjsonadapter)
17. [Library dependencies](#dependencies)

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

```kotlin
editText.setOnDoneClickedListener {
    Log.e("a", "enter pressed")
}
```

#### ViewX <a name="viewx"></a>

View eXtension methods.

LongClick listener without having to return an boolean representing if whe SDK should further consume the touch event:

```kotlin
setOnLongKlickListener {
}
```

```kotlin
setOnLongKlickListener(shouldConsumeTouchEvent = false) {
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

```kotlin
editText.openKeyboard()
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

#### Color <a name="color"></a>

I don't know why in Android SDK, colors are represented as simple integers, and not objects of some ``Color`` class. This library provides a wrapper ``Color`` class around color integers:

```kotlin
import ro.dobrescuandrei.utils.Color
```

```kotlin
var sdkColor : Int=android.graphics.Color.BLACK
var color=Color(sdkColor) //convert from int color to Color object
color=Color(code = "#000000")
color=Color(red = 100, green = 123, blue = 255)
color=Color(red = 100, green = 123, blue = 255, alpha = 100)
println("components: ${color.red} ${color.green} ${color.blue} ${color.alpha}")
println("color is ${if (color.isDark()) "dark" else "light"}")
color=Colors.Black //predefined colors, similar to Color.BLACK
sdkColor=color.value //convert from Color object to int color
```

```kotlin
//get/setKolor -> instances of Color class
//SDK's get/setColor -> ints :|
val color : Color=context.getKolor(R.color.blue)
textView.setBackgroundKolor(color)
textView.setTextKolor(color)
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
                RetrofitUtils.downloadFile(response.body()!!, outputPath = AppFileManager.getSampleFilePath())
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
ApiClient.Instance.uploadPdf(RetrofitUtils.fileUpload(context = this, path = AppFileManager.getSampleFilePath()))
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

#### JodaTime extensions <a name="jodatimex"></a>

Nil ``DateTime`` / ``LocalDate`` / ``LocalTime`` support (null object pattern) and ``Calendar`` conversions

```kotlin
var jodaDateTime : DateTime = NilDateTime //01.01.1970
println("${jodaDateTime.isNil()} ${jodaDateTime.isNotNil()}")
val calendar : Calendar = jodaDateTime.toCalendar()
jodaDateTime=calendar.toDateTime()
```

``DateTimeFormatter`` is an empty ``SimpleDateFormatter`` wrapper. You need to customise this class with your formatting use cases by adding your own extension methods:

```kotlin
fun DateTimeFormatter.date() = formatWith(pattern = "dd.MM.yyyy")
fun DateTimeFormatter.dateTime() = formatWith(pattern = "dd.MM.yyyy HH:mm")
```

And then:

```kotlin
println("Date is ${jodaDateTime.format().dateTime()}")
println("Date is ${jodaLocalDate.format().date()}")
```

Gson type adapters to convert ``DateTime`` / ``LocalDate`` / ``LocalTime`` to json string:

```kotlin
val gson : Gson = GsonBuilder()
    .registerTypeAdapter(DateTime::class.java, DateTimeJsonAdapter("dd.MM.yyyy HH:mm"))
    .registerTypeAdapter(LocalDate::class.java, LocalDateJsonAdapter("dd.MM.yyyy"))
    .registerTypeAdapter(LocalTime::class.java, LocalTimeJsonAdapter("HH:mm"))
    .create()
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
            yieldAll(findFilesIn(directory = file))
        else yield(file)
    }
}
```

#### CollectionX <a name="collection"></a>

``Collection`` extensions:

```kotlin
val list=listOf(1, 2, 3, null, 3)
val set=list.mapToSet { it } //setOf(1,2,3)
```

#### TypedArrayX <a name="typedarray"></a>

``TypedArray`` extensions:

```kotlin
constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
{
    val args : TypedArray=context.obtainStyledAttributes(attrs, R.styleable.SampleCustomView)
    val someBoolean : Boolean=args.getBoolean(R.styleable.SampleCustomView_scv_someBoolean, false)
    val someInt : Int=args.getInt(R.styleable.SampleCustomView_scv_someInt, 0)
    val someFloat : Float=args.getFloat(R.styleable.SampleCustomView_scv_someFloat, 0f)
    val someColor : Color=Color(args.getColor(R.styleable.SampleCustomView_scv_someColor, Colors.Black.value))
    val someDimension : Float=args.getDimension(R.styleable.SampleCustomView_scv_someDimension, 0f)
    val someDimensionInPx : Int=args.getDimensionPixelSize(R.styleable.SampleCustomView_scv_someDimension, 0)
    val someResourceId : Int=args.getResourceId(R.styleable.SampleCustomView_scv_someResource, 0)
    args.recycle()
}
```

Is equivalent to:

```kotlin
constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
{
    val args : TypedArray=context.obtainStyledAttributes(attrs, R.styleable.SampleCustomView)
    val someBoolean : Boolean=args.getBoolean(R.styleable.SampleCustomView_scv_someBoolean)?:false
    val someInt : Int=args.getInt(R.styleable.SampleCustomView_scv_someInt)?:0
    val someFloat : Float=args.getFloat(R.styleable.SampleCustomView_scv_someFloat)?:0f
    val someColor : Color=args.getKolor(R.styleable.SampleCustomView_scv_someColor)?:Colors.Black
    val someDimension : Float=args.getDimension(R.styleable.SampleCustomView_scv_someDimension)?:0f
    val someDimensionInPx : Int=args.getDimensionInPixels(R.styleable.SampleCustomView_scv_someDimension)?:0
    val someResourceId : Int?=args.getResourceId(R.styleable.SampleCustomView_scv_someResource)
    args.recycle()
}
```

#### StringJsonAdapter (Gson) <a name="stringjsonadapter"></a>

``StringJsonAdapter`` is a Gson ``TypeAdapter`` that converts non-nullable strings:

```kotlin
class DemoJson(val some : String, val another : String)
```

```kotlin
val gson : Gson = GsonBuilder()
    .registerTypeAdapter(String::class.java, StringJsonAdapter(defaultValue = "-"))
    .create()

var json="{\"some\": null, \"another\": \"test\"}"
val obj=gson.fromJson(json, DemoJson::class.java) //obj.some="-"; obj.another="test"
json=gson.toJson(obj) //json={"some": null, "another": "test"}
```

#### Library dependencies <a name="dependencies"></a>

This library depends on the following libraries:

```
implementation "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
implementation 'androidx.appcompat:appcompat:1.1.0-alpha04'
implementation 'com.google.android.material:material:1.1.0-alpha05'
implementation 'androidx.recyclerview:recyclerview:1.1.0-alpha04'
implementation 'com.balysv.materialmenu:material-menu:2.0.0'
implementation 'com.squareup.okhttp3:okhttp:3.12.0'
implementation 'net.danlew:android.joda:2.10.1.2'
implementation 'com.google.code.gson:gson:2.8.5'
```

You can exclude any of those:

```
implementation ('com.github.andob:DobDroidMiscUtils:v1.1.7') {
    exclude group: 'com.android.support'
    exclude group: 'com.balysv.materialmenu'
    exclude group: 'com.squareup.okhttp3'
    exclude group: 'joda-time'
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


[![FOSSA Status](https://app.fossa.io/api/projects/git%2Bgithub.com%2Fandob%2FDobDroidMiscUtils.svg?type=large)](https://app.fossa.io/projects/git%2Bgithub.com%2Fandob%2FDobDroidMiscUtils?ref=badge_large)