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
    //GET
    @GET("users/{uName}")
    suspend fun getUser(@Path("uName") username : String) : Response<UserResponse>
    @GET("users")
    suspend fun getUsersLike(@Query("search") searchString : String,@Query("except") except : String) : Response<Users>
    @GET("users/login")
    suspend fun getLoginUser(@Query("username") username : String, @Query("password") password : String) : Response<UserResponse>
    @GET("/userExists")
    suspend fun  checkUsernameAvailability(@Query("uName") username : String) : Response<MessageResponse>
    @GET("users/pendingFriends/{uName}")
    suspend fun getFriendRequests(@Path("uName") username: String) : Response<Users>
    @GET("users/getFriends/{uName}")
    suspend fun getFriends(@Path("uName") username: String) : Response<Users>

    //POST
    @POST("users")
    suspend fun createUser(@Body user : UserResponse) : Response<MessageResponse>
    @POST("recoverPassword")
    suspend fun recoverPassword(@Query("username") username : String, @Query("code") code : String) : Response<MessageResponse>
    @POST("verifyMail")
    suspend fun sendVerificationMail(@Query("email") email : String, @Query("code") code : String) : Response<MessageResponse>
    @POST("users/sendRequest")
    suspend fun sendFriendRequest(@Query("username") username: String,@Query("uNameReq") fromUsername : String) : Response<MessageResponse>
    @POST("users/addFriend")
    suspend fun acceptFriendRequest(@Query("username")usernameAccepts: String, @Query("uNameReq") user : String) : Response<Users>

    //PUT
    @PUT("users/newPassword")
    suspend fun changePassword(@Query("username") username: String,@Query("password") newPassword : String) : Response<MessageResponse>
    @PUT("users/{uName}")
    suspend fun updateUser(@Path("uName") username: String, @Body user : UserResponse) : Response<MessageResponse>

    //DELETE
    @DELETE("users/deleteRequest")
    suspend fun declineFriendRequest(@Query("username") usernameDeclines: String, @Query("uNameReq") user : String) : Response<Users>
}

object TapasTopApi{
    val retrofitService : TapasTopApiService by lazy { retrofit.create(TapasTopApiService::class.java)}
}