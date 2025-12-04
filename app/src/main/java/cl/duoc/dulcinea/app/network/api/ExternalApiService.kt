package cl.duoc.dulcinea.app.network.api

import cl.duoc.dulcinea.app.network.model.ExternalPost
import retrofit2.Response
import retrofit2.http.GET

interface ExternalApiService {

    @GET("posts")
    suspend fun getExternalPosts(): Response<List<ExternalPost>>
}