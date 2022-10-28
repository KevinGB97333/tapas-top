package es.upm.isii.tapastop.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL=""

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface TapasTopApiService{

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username : String) : User
}

object TapasTopApi{
    val retrofitService : TapasTopApiService by lazy { retrofit.create(TapasTopApiService::class.java)}
}