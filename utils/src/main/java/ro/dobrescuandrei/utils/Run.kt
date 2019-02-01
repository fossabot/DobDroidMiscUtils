package ro.dobrescuandrei.utils

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Handler
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

object Run
{
    lateinit var handler : Handler

    var globalErrorHandler : ((Exception) -> (Unit))? = null

    fun onUIThread(toRun : () -> Unit)
    {
        handler.post(toRun)
    }

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
        Thread {
            try
            {
                val result = task()

                Run.onUIThread {
                    onAny?.invoke()
                    onSuccess?.invoke(result)
                }
            }
            catch (ex : Exception)
            {
                Run.onUIThread {
                    onAny?.invoke()
                    onError?.invoke(ex)
                    globalErrorHandler?.invoke(ex)
                }
            }
        }.start()
    }

    @SuppressLint("StaticFieldLeak")
    fun paralel(maximumNumberOfThreads : Int = 4,
                numberOfRunningThreads : AtomicInteger = AtomicInteger(0),
                tasks : MutableList<() -> (Any?)>,
                onDone : (() -> (Unit))? = null,
                onError : ((Exception) -> (Unit))? = null)
    {
        val runnableQueue=LinkedBlockingQueue<Runnable>(128)
        val threadPoolExecutor=ThreadPoolExecutor(maximumNumberOfThreads, maximumNumberOfThreads, 1, TimeUnit.MINUTES, runnableQueue)
        threadPoolExecutor.allowCoreThreadTimeOut(true)

        numberOfRunningThreads.set(tasks.size)

        for (task in tasks)
        {
            object : AsyncTask<() -> (Any?), Void, Exception?>()
            {
                override fun doInBackground(vararg params : (() -> Any?)?) : Exception?
                {
                    try
                    {
                        params[0]?.invoke()
                        return null
                    }
                    catch (ex : Exception)
                    {
                        return ex
                    }
                }

                override fun onPostExecute(error : Exception?)
                {
                    super.onPostExecute(error)

                    numberOfRunningThreads.set(numberOfRunningThreads.get()-1)

                    if (error!=null)
                    {
                        onError?.invoke(error)
                        globalErrorHandler?.invoke(error)
                    }
                    else if (numberOfRunningThreads.get()==0)
                    {
                        onDone?.invoke()
                    }
                }
            }.executeOnExecutor(threadPoolExecutor, task)
        }
    }
}
