package ro.dobrescuandrei.dobdroidmiscutils

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

abstract class ApiClient
{
    companion object
    {
        val Instance : IApiClient by lazy {
            Retrofit.Builder()
                .baseUrl("http://www.pdf995.com/")
                .client(OkHttpClient.Builder()
                    .followRedirects(true)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build())
                .build()
                .create(IApiClient::class.java)
        }
    }
}
