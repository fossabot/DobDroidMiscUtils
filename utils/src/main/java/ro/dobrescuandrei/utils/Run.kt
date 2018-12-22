package ro.dobrescuandrei.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.os.AsyncTask
import android.os.Handler
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.atomic.AtomicInteger

object Run
{
    lateinit var handler : Handler

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
                }
            }
        }.start()
    }

    @SuppressLint("StaticFieldLeak")
    fun paralel(maximumNumberOfThreads : Int = 8,
                numberOfRunningThreads : AtomicInteger = AtomicInteger(0),
                tasks : MutableList<() -> (Any?)>,
                onDone : DispatchOnce? = null,
                onError : ((Exception) -> (Unit))? = null)
    {
        val numberOfThreadsToStart=maximumNumberOfThreads-numberOfRunningThreads.get()
        for (i in 1..numberOfThreadsToStart)
        {
            if (tasks.isEmpty())
                break

            Thread {
                var error : Exception? = null

                try
                {
                    tasks.removeAt(index = 0)()
                }
                catch (ex : Exception)
                {
                    error=ex
                }
                finally
                {
                    numberOfRunningThreads.decrementAndGet()

                    if ((error is SocketTimeoutException)||(error is UnknownHostException))
                    {
                        tasks.clear()

                        Run.onUIThread {
                            onError?.invoke(error)
                        }
                    }
                    else if (numberOfRunningThreads.get()==0&&tasks.isEmpty())
                    {
                        Run.onUIThread {
                            onDone?.invoke()
                        }
                    }
                    else
                    {
                        Run.paralel(
                            maximumNumberOfThreads = maximumNumberOfThreads,
                            numberOfRunningThreads = numberOfRunningThreads,
                            tasks = tasks,
                            onDone = onDone,
                            onError = onError)
                    }
                }
            }.start()

            numberOfRunningThreads.incrementAndGet()
        }
    }
}
