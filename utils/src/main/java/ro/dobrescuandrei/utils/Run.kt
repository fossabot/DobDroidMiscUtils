package ro.dobrescuandrei.utils

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicInteger

object Run
{
    fun delayed(delay: Long, toRun: () -> (Unit))
    {
        Handler().postDelayed(toRun, delay)
    }

    @SuppressLint("StaticFieldLeak")
    fun <T> async(onAny : (() -> (Unit))? = null,
                  onError : ((Exception) -> (Unit))? = null,
                  onSuccess : ((T) -> (Unit))? = null,
                  task: () -> (T))
    {
        object : AsyncTask<() -> (T), Void, Any?>()
        {
            override fun doInBackground(vararg params: (() -> T)?): Any?
            {
                try
                {
                    return params[0]!!()
                }
                catch (ex : Exception)
                {
                    return ex
                }
            }

            override fun onPostExecute(result: Any?)
            {
                onAny?.invoke()

                if (result==null)
                    onError?.invoke(RuntimeException())
                else if (result is Exception)
                    onError?.invoke(result)
                else onSuccess?.invoke(result as T)
            }
        }.execute(task)
    }
}
