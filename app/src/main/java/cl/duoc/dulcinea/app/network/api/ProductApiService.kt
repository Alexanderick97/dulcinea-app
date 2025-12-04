package cl.duoc.dulcinea.app.network.api

import cl.duoc.dulcinea.app.network.model.ApiProduct
import cl.duoc.dulcinea.app.network.model.ApiProductResponse
import cl.duoc.dulcinea.app.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {

    @GET("products")
    suspend fun getAllProducts(): Response<ApiProductResponse>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ApiProduct>

    @POST("products")
    suspend fun createProduct(@Body product: ApiProduct): Response<ApiProduct>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: ApiProduct): Response<ApiProduct>

    @DELETE("products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse>
}