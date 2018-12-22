package ro.dobrescuandrei.dobdroidmiscutils

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ro.dobrescuandrei.utils.*
import java.lang.RuntimeException
import java.util.concurrent.atomic.AtomicReference

class MainActivity : AppCompatActivity()
{
    val permissionsHandler = PermissionsHandler()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        toolbar.title="Title"
        toolbar.subtitle="Subtitle"
//        toolbar.setupBackIcon()

        toolbar.setMenu(R.menu.menu_main)
        toolbar[R.id.add] = {
            Log.e("a", "add logic")
        }

        toolbar.setupHamburgerMenu()
        toolbar.setOnHamburgerMenuClickedListener {
            Log.e("a", "toggle drawer")
        }

        ScreenSize.init(withContext = this)

        if (ScreenSize.width<ScreenSize.height)
            if (ScreenSize.density>=2.0)
                Log.e("a", "a")

        permissionsHandler.askFor(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            .onGranted { Log.e("a", "YAY") }
            .onDenied { Log.e("a", "NAY") }
            .withContext(context = this)

        Handler().postDelayed({
            Keyboard.open(on = this)
            Handler().postDelayed({
                Keyboard.close(on = this)
            }, 1000)
        }, 1000)

        editText.setOnTextChangedListener { newText ->
            Log.e("a", newText)
        }

        editText.setOnEditorActionListener { actionId ->
            if (actionId==EditorInfo.IME_ACTION_DONE)
                Log.e("a", "enter pressed")
        }

//        val viewPager=ViewPager(this)
//        viewPager.setOnPageChangedListener { page ->
//            Log.e("a", "You're on page $page")
//        }
//
//        val bottomNavigationView=BottomNavigationView(this)
//        bottomNavigationView.setupWithViewPager(viewPager)
//        bottomNavigationView.setupWithViewPager(viewPager, initialTab = 0)

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

        ApiClient.Instance.uploadPdf(fileUpload(context = this,
            path = AppFileManager.getSampleFilePath()))
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

        Run.delayed(1000) {
            println("delayed on ui")
        }

        Run.async {
            println("on new thread")
        }

        Run.async(task = {
            println("on new thread with result")
            Thread.sleep(1000)
            return@async 1
        },
        onAny = { println("hide loading dialog") },
        onError = { it.printStackTrace() },
        onSuccess = { println("result: $it") })

        Run.async(task = {
            println("on new thread without result")
            Thread.sleep(1000)
        },
        onSuccess = { println("done!") })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionsHandler.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        toolbar.onCreateOptionsMenu(menuInflater, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?) : Boolean
    {
        toolbar.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }
}
