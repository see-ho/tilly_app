package com.seeho.tilly.core.network

import com.seeho.tilly.core.network.model.OpenAIChatRequest
import com.seeho.tilly.core.network.model.OpenAIChatResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    suspend fun createChatCompletion(
        @Body request: OpenAIChatRequest
    ): OpenAIChatResponse
}
