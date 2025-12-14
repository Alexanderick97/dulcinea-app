package cl.duoc.dulcinea.app.network.api

import cl.duoc.dulcinea.app.network.model.ApiResponse
import cl.duoc.dulcinea.app.network.model.ApiUser
import cl.duoc.dulcinea.app.network.model.LoginRequest
import cl.duoc.dulcinea.app.network.model.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface UserApiService {
    @POST("api/users/register")
    suspend fun register(@Body user: ApiUser): Response<ApiResponse>
    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("api/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): Response<ApiUser>
    @PUT("api/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: ApiUser): Response<ApiUser>
    @GET("api/users/email/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): Response<ApiUser>
}