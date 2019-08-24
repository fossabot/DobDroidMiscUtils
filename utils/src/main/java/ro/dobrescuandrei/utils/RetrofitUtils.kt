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

    fun newSocketFactoryBypassingConnectivityHealthChecks() : SocketFactory = object : SocketFactory()
    {
        override fun createSocket(host: String?, port: Int): Socket = SocketBypassingOkHttpConnectivityChecks(host, port)
        override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket = SocketBypassingOkHttpConnectivityChecks(host, port, localHost, localPort)
        override fun createSocket(host: InetAddress?, port: Int): Socket = SocketBypassingOkHttpConnectivityChecks(host, port)
        override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket = SocketBypassingOkHttpConnectivityChecks(address, port, localAddress, localPort)
    }
}

private class SocketBypassingOkHttpConnectivityChecks : Socket
{
    constructor() : super()
    constructor(proxy: Proxy?) : super(proxy)
    constructor(impl: SocketImpl?) : super(impl)
    constructor(host: String?, port: Int) : super(host, port)
    constructor(address: InetAddress?, port: Int) : super(address, port)
    constructor(host: String?, port: Int, localAddr: InetAddress?, localPort: Int) : super(host, port, localAddr, localPort)
    constructor(address: InetAddress?, port: Int, localAddr: InetAddress?, localPort: Int) : super(address, port, localAddr, localPort)
    constructor(host: String?, port: Int, stream: Boolean) : super(host, port, stream)
    constructor(host: InetAddress?, port: Int, stream: Boolean) : super(host, port, stream)

    override fun getInputStream(): InputStream = InputStreamBypassingOkHttpConnectivityChecks(super.getInputStream())
}

class InputStreamBypassingOkHttpConnectivityChecks(private val delegate: InputStream) : InputStream()
{
    @Throws(IOException::class) override fun read(): Int = delegate.read()
    @Throws(IOException::class) override fun read(b: ByteArray): Int = delegate.read(b)
    @Throws(IOException::class) override fun skip(n: Long): Long = delegate.skip(n)
    @Throws(IOException::class) override fun available(): Int =delegate.available()
    @Throws(IOException::class) override fun close() = delegate.close()
    @Synchronized override fun mark(readlimit: Int) = delegate.mark(readlimit)
    @Synchronized @Throws(IOException::class) override fun reset() = delegate.reset()
    override fun markSupported(): Boolean = delegate.markSupported()
    override fun hashCode(): Int = delegate.hashCode()
    override fun equals(obj: Any?): Boolean = delegate == obj
    override fun toString(): String = delegate.toString()

    @Throws(IOException::class)
    override fun read(b: ByteArray, off: Int, len: Int): Int
    {
        try
        {
            throw RuntimeException("HACK")
        }
        catch (ex: Exception)
        {
            val stackTrace = ex.stackTrace
            for (element in stackTrace)
            {
                if (element.className=="okhttp3.internal.connection.RealConnection"&&
                    element.methodName=="isHealthy")
                {
                    reset()
                    return 4
                }
            }
        }

        return delegate.read(b, off, len)
    }
}
