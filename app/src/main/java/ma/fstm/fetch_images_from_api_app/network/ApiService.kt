package ma.fstm.fetch_images_from_api_app.network

import ma.fstm.fetch_images_from_api_app.models.Property
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// The base URL of the API
private const val BASE_URL = "http://172.20.10.2:3000/horizontal/"

// Creating an instance of Moshi for JSON parsing
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// Creating an instance of Retrofit for making API calls
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Interface defining the API endpoints
interface ApiService {
    @GET(".")
    fun getAllData(): Call<List<Property>>
}

// Singleton object for accessing the Retrofit service
object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}