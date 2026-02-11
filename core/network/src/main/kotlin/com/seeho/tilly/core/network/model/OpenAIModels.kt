package com.seeho.tilly.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAIChatRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<OpenAIMessage>,
    @SerialName("max_tokens") val maxTokens: Int = 1000,
    val temperature: Double = 0.7
)

@Serializable
data class OpenAIMessage(
    val role: String,
    val content: String
)

@Serializable
data class OpenAIChatResponse(
    val id: String,
    val choices: List<OpenAIChoice>
)

@Serializable
data class OpenAIChoice(
    val index: Int,
    val message: OpenAIMessage,
    @SerialName("finish_reason") val finishReason: String
)
