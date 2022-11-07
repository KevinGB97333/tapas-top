package es.upm.isii.tapastop.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL="https://tapas-top-rest-api.herokuapp.com/"

var mHttpLoggingInterceptor = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)

var mOkHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(mHttpLoggingInterceptor)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(mOkHttpClient)
    .build()

interface TapasTopApiService{

    @GET("users/{username}")
    suspend fun getUser(@Path("username") username : String) : User


    @GET("users")
    suspend fun getLoginUser(@Query("username") username : String, @Query("password") password : String) : Response<UserResponse>

    @POST("users")
    suspend fun createUser(@Body user : UserResponse) : Response<CreateResponse>


    @PUT("users/{username}")
    suspend fun updateUser(@Path("username") username: String, @Body user : User)

}

object TapasTopApi{
    val retrofitService : TapasTopApiService by lazy { retrofit.create(TapasTopApiService::class.java)}
}