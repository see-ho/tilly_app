package com.seeho.tilly.core.network

import com.seeho.tilly.core.network.model.OpenAIChatRequest
import com.seeho.tilly.core.network.model.OpenAIMessage
import com.seeho.tilly.core.network.model.TilAnalysisResult
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.serializer
import kotlinx.serialization.builtins.ListSerializer
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.cancellation.CancellationException

@Singleton
class OpenAIService @Inject constructor(
    private val api: OpenAIApi,
    private val json: Json
) {
    /**
     * TIL 내용을 OpenAI로 분석하여 결과를 반환
     * @return Result<TilAnalysisResult> — 성공 시 분석 결과, 실패 시 예외 정보 포함
     */
    suspend fun analyzeTil(
        title: String,
        learned: String,
        difficulty: String?,
        tomorrow: String?
    ): Result<TilAnalysisResult> {

        val prompt = """
당신은 풀스택 전문 테크 리드(Tech Lead)이자 학습 코치입니다. 
제공된 TIL 내용을 바탕으로 '기술적 통찰력'이 담긴 분석을 수행하세요.

[TIL 데이터]
- 제목: $title
- 오늘 배운 것: $learned
- 어려웠던 점: ${difficulty ?: "없음"}
- 내일 할 일: ${tomorrow ?: "없음"}

[분석 가이드라인 - 반드시 준수할 것]
1. context_understanding: 
   - 사용자가 겪은 기술적 고충(Version Conflict, Dependency Bloat 등)을 정확히 파악할 것.

2. tags (Strict Rules):
   - 'Learning', 'Development' 같은 범용적이고 무의미한 단어는 절대 금지**.
   - 반드시 본문에 언급된 구체적인 라이브러리, API, 프레임워크, 아키텍처 명칭을 사용하십시오.
   - 예: #Compose, #FlowRow, #Material3, #Multi-Module, #Convention-Plugin, #Dependency-Management
   - 영어로 작성하며, 대소문자를 정확히 지킬 것.
   - 최소 1개 이상 5개 이하로 추출할 것.

3. feedback:
   - 영혼 없는 칭찬 금지. 
   - 사용자의 성장을 격려하는 시니어 개발자의 따뜻한 조언을 한국어로 3문장 이상 작성하세요.

[출력 형식]
반드시 아래 형식의 JSON 데이터만 출력하세요 (마크다운 없이 순수 문자열만):
{
    "emotion": "성취감/만족/평범/어려움/좌절 중 택 1",
    "emotionScore": 1~5 정수,
    "difficultyLevel": "EASY/NORMAL/HARD/VERY_HARD",
    "feedback": "문맥이 담긴 조언",
    "tags": ["Tag1", "Tag2", "Tag3"]
}
""".trimIndent()

        val request = OpenAIChatRequest(
            messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )
        )

        return try {
            val response = api.createChatCompletion(request)
            val content = response.choices.firstOrNull()?.message?.content
                ?: return Result.failure(Exception("OpenAI 응답이 비어있습니다"))

            val cleanJson = content.replace("```json", "").replace("```", "").trim()
            val jsonElement = json.parseToJsonElement(cleanJson)

            val jsonObject = jsonElement as? JsonObject
                ?: return Result.failure(Exception("OpenAI 응답이 JSON 객체가 아닙니다: $cleanJson"))

            Result.success(
                TilAnalysisResult(
                    emotion = jsonObject["emotion"]?.jsonPrimitive?.content ?: "평범",
                    emotionScore = jsonObject["emotionScore"]?.jsonPrimitive?.intOrNull ?: 3,
                    difficultyLevel = jsonObject["difficultyLevel"]?.jsonPrimitive?.content ?: "NORMAL",
                    feedback = jsonObject["feedback"]?.jsonPrimitive?.content ?: "오늘도 수고하셨어요!",
                    tags = jsonObject["tags"]?.let {
                        json.decodeFromJsonElement(ListSerializer(serializer<String>()), it)
                    } ?: emptyList()
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
