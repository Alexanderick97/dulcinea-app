package cl.duoc.dulcinea.app.network

import cl.duoc.dulcinea.app.network.api.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {

    private const val BASE_URL_LOCAL_USER = "http://10.0.2.2:8081/"
    private const val BASE_URL_LOCAL_PRODUCT = "http://10.0.2.2:8082/"
    private const val BASE_URL_EXTERNAL = "https://jsonplaceholder.typicode.com/"

    // Configurar logging
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Client para desarrollo local (permite HTTP)
    private val localClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Client para APIs externas
    private val externalClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Retrofit para User Service
    private val userRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_LOCAL_USER)
        .client(localClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Retrofit para Product Service
    private val productRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_LOCAL_PRODUCT)
        .client(localClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Retrofit para APIs externas
    private val externalRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_EXTERNAL)
        .client(externalClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Servicios locales
    val userApiService: UserApiService by lazy {
        userRetrofit.create(UserApiService::class.java)
    }

    val productApiService: ProductApiService by lazy {
        productRetrofit.create(ProductApiService::class.java)
    }

    val externalApiService: ExternalApiService by lazy {
        externalRetrofit.create(ExternalApiService::class.java)
    }
}