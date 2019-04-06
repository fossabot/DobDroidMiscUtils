package ro.dobrescuandrei.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsHandler
{
    private var permissions : Array<String>?  = null
    private var onGranted   : (() -> (Unit))? = null
    private var onDenied    : (() -> (Unit))? = null

    fun askFor(permissions : Array<String>?) : PermissionsHandler
    {
        this.permissions=permissions
        return this
    }

    fun onGranted(onGranted : (() -> (Unit))?) : PermissionsHandler
    {
        this.onGranted=onGranted
        return this
    }

    fun onDenied(onDenied : (() -> (Unit))?) : PermissionsHandler
    {
        this.onDenied=onDenied
        return this
    }

    fun withContext(context : Activity)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            var atLeastOneIsDenied = false
            var pressedNeverAskAgain = false

            if (permissions!=null)
            {
                loop@ for (permission in permissions!!)
                {
                    if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    {
                        if (context.shouldShowRequestPermissionRationale(permission))
                            pressedNeverAskAgain = true
                        atLeastOneIsDenied = true
                        break@loop
                    }
                }

                when
                {
                    pressedNeverAskAgain -> callOnDenied()
                    atLeastOneIsDenied -> ActivityCompat.requestPermissions(context, permissions!!, 0)
                    else -> callOnGranted()
                }
            }
        }
        else
        {
            callOnGranted()
        }
    }

    fun onRequestPermissionsResult(requestCode : Int, permissions: Array<out String>, grantResults : IntArray)
    {
        for (grantResult in grantResults)
        {
            if (grantResult != PackageManager.PERMISSION_GRANTED)
            {
                callOnDenied()
                return
            }
        }

        callOnGranted()
    }

    private fun callOnGranted()
    {
        onGranted?.invoke()
        onGranted=null

        onDenied=null
        permissions=null
    }

    private fun callOnDenied()
    {
        onDenied?.invoke()
        onDenied=null

        onGranted=null
        permissions=null
    }
}