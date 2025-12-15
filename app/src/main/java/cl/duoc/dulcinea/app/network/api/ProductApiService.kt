package cl.duoc.dulcinea.app.network.api

import cl.duoc.dulcinea.app.network.model.ApiProduct
import cl.duoc.dulcinea.app.network.model.ApiProductResponse
import cl.duoc.dulcinea.app.network.model.ApiResponse
import retrofit2.Response
import retrofit2.http.*

interface ProductApiService {

    @GET("api/products/")
    suspend fun getAllProducts(): Response<ApiProductResponse>

    @GET("api/products/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<ApiProduct>

    @POST("api/products/")
    suspend fun createProduct(@Body product: ApiProduct): Response<ApiProduct>

    @PUT("api/products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: ApiProduct): Response<ApiProduct>

    @DELETE("api/products/{id}")
    suspend fun deleteProduct(@Path("id") id: Int): Response<ApiResponse>

    // Nuevos endpoints
    @GET("api/products/search")
    suspend fun searchProducts(@Query("name") name: String): Response<ApiProductResponse>

    @GET("api/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): Response<ApiProductResponse>

    @POST("api/products/{id}/reduce-stock")
    suspend fun reduceStock(
        @Path("id") id: Int,
        @Query("quantity") quantity: Int
    ): Response<ApiProduct>

    @GET("api/products/health")
    suspend fun getHealth(): Response<Map<String, String>>
}