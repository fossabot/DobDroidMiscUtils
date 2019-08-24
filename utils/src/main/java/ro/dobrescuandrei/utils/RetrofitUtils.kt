package ro.dobrescuandrei.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.internal.Util
import okio.BufferedSink
import okio.Okio
import okio.Source
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.net.InetAddress
import java.net.Proxy
import java.net.Socket
import java.net.SocketImpl
import javax.net.SocketFactory


object RequestBodyForContentUri
{
    fun create(contentType : MediaType?, uri : String, contentResolver : ContentResolver) : RequestBody
    {
        return object : RequestBody()
        {
            override fun contentType(): MediaType? = contentType

            override fun contentLength(): Long
            {
                var stream : InputStream? = null
                try
                {
                    stream=contentResolver.openInputStream(Uri.parse(uri))
                    return stream?.available()?.toLong()?:0
                }
                catch (e : Exception)
                {
                    return 0
                }
                finally
                {
                    try
                    {
                        stream?.close()
                    }
                    catch (ex : Exception)
                    {
                    }
                }
            }

            override fun writeTo(sink: BufferedSink?)
            {
                var source : Source? = null
                try
                {
                    val stream=contentResolver.openInputStream(Uri.parse(uri))
                    source=Okio.source(stream)
                    sink?.writeAll(source)
                }
                catch (e : Exception)
                {
                }
                finally
                {
                    if (source!=null)
                        Util.closeQuietly(source)
                }
            }

        }
    }
}

object RetrofitUtils
{
    fun fileUpload(context : Context, fileKey : String = "image", path : String) : MultipartBody.Part
    {
        val fileName = FileManager.removeInvalidCharactersFromFileName(path.split("/").last())
        val contentType = MediaType.parse("multipart/form-data")
        val requestFile : RequestBody = if (path.startsWith("content://"))
            RequestBodyForContentUri.create(contentType, path, context.contentResolver)
        else RequestBody.create(contentType, File(path))
        return MultipartBody.Part.createFormData(fileKey, fileName, requestFile)
    }

    @Throws(Exception::class)
    fun downloadFile(body : ResponseBody, outputPath : String)
    {
        val sink = Okio.buffer(Okio.sink(File(outputPath)))
        sink.writeAll(body.source())
        sink.close()
    }
}
