package ro.dobrescuandrei.dobdroidmiscutils

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface IApiClient
{
    @Streaming
    @GET("/samples/pdf.pdf")
    fun downloadPdf() : Call<ResponseBody>

    @Multipart
    @POST("/samples/pdf.pdf")
    fun uploadPdf(@Part file : MultipartBody.Part) : Call<Unit?>
}