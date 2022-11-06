package es.upm.isii.tapastop.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL="https://tapas-top-rest-api.herokuapp.com/"

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


    @GET("users")
    suspend fun getLoginUser(@Query("username") username : String, @Query("password") password : String) : UserResponse

    @POST("users/")
    suspend fun createUser(@Body user : User)

    @PUT("users/{username}")
    suspend fun updateUser(@Path("username") username: String, @Body user : User)

}

object TapasTopApi{
    val retrofitService : TapasTopApiService by lazy { retrofit.create(TapasTopApiService::class.java)}
}