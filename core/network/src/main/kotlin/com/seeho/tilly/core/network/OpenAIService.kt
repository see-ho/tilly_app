package com.seeho.tilly.core.network

import com.seeho.tilly.core.network.model.OpenAIChatRequest
import com.seeho.tilly.core.network.model.OpenAIMessage
import com.seeho.tilly.core.network.model.RetrospectiveResult
import com.seeho.tilly.core.network.model.TilAnalysisResult
import com.seeho.tilly.core.network.model.TilSummaryForPrompt
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
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

        val prompt = TIL_ANALYSIS_PROMPT_TEMPLATE.format(
            title, learned, difficulty ?: "없음", tomorrow ?: "없음"
        )

        val request = OpenAIChatRequest(
            messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )
        )

        return try {
            val response = api.createChatCompletion(request)
            val jsonObject = parseOpenAIJsonResponse(response)
                ?: return Result.failure(Exception("OpenAI 응답이 비어있거나 JSON 객체가 아닙니다"))

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

    /**
     * 월간 TIL 데이터를 기반으로 GPT 회고를 생성
     * @return Result<RetrospectiveResult> — 성공 시 회고 결과, 실패 시 예외 정보 포함
     */
    suspend fun generateRetrospective(
        month: Int,
        year: Int,
        tilSummaries: List<TilSummaryForPrompt>,
    ): Result<RetrospectiveResult> {

        val tilDataText = tilSummaries.joinToString("\n") { til ->
            """
            - 제목: ${til.title}
            - 배운 것: ${til.learned}
            - 어려웠던 점: ${til.difficulty ?: "없음"}
            - 감정: ${til.emotion} (점수: ${til.emotionScore}/5)
            - 난이도: ${til.difficultyLevel}
            - 태그: ${til.tags.joinToString(", ")}
            ---
            """.trimIndent()
        }

        val prompt = RETROSPECTIVE_PROMPT_TEMPLATE.format(
            year, month, month, tilSummaries.size, tilDataText
        )

        val request = OpenAIChatRequest(
            messages = listOf(
                OpenAIMessage(role = "user", content = prompt)
            )
        )

        return try {
            val response = api.createChatCompletion(request)
            val jsonObject = parseOpenAIJsonResponse(response)
                ?: return Result.failure(Exception("OpenAI 응답이 비어있거나 JSON 객체가 아닙니다"))

            Result.success(
                RetrospectiveResult(
                    summary = jsonObject["summary"]?.jsonPrimitive?.content ?: "",
                    growthPoints = jsonObject["growthPoints"]?.jsonArray
                        ?.map { it.jsonPrimitive.content } ?: emptyList(),
                    improvementPoints = jsonObject["improvementPoints"]?.jsonArray
                        ?.map { it.jsonPrimitive.content } ?: emptyList(),
                    nextMonthSuggestions = jsonObject["nextMonthSuggestions"]?.jsonArray
                        ?.map { it.jsonPrimitive.content } ?: emptyList(),
                )
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** OpenAI 응답에서 JSON 객체를 파싱하는 공통 함수 */
    private fun parseOpenAIJsonResponse(
        response: com.seeho.tilly.core.network.model.OpenAIChatResponse,
    ): JsonObject? {
        val content = response.choices.firstOrNull()?.message?.content ?: return null
        val cleanJson = content.replace("```json", "").replace("```", "").trim()
        val jsonElement = json.parseToJsonElement(cleanJson)
        return jsonElement as? JsonObject
    }

    companion object {
        /** TIL 분석 프롬프트 템플릿 (%s: 제목, 배운것, 어려웠던점, 내일할일) */
        private val TIL_ANALYSIS_PROMPT_TEMPLATE = """
당신은 풀스택 전문 테크 리드(Tech Lead)이자 학습 코치입니다. 
제공된 TIL 내용을 바탕으로 '기술적 통찰력'이 담긴 분석을 수행하세요.

[TIL 데이터]
- 제목: %s
- 오늘 배운 것: %s
- 어려웠던 점: %s
- 내일 할 일: %s

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

        /** 월간 회고 프롬프트 템플릿 (%d: year, month, month, size, %s: tilDataText) */
        private val RETROSPECTIVE_PROMPT_TEMPLATE = """
당신은 풀스택 전문 테크 리드(Tech Lead)이자 학습 코치입니다. 
아래는 사용자가 %d년 %d월에 작성한 TIL(Today I Learned) 데이터입니다.
이 데이터를 종합 분석하여 월간 학습 회고를 작성해주세요.

[%d월 TIL 데이터 (총 %d개)]
%s

[어투 가이드라인]
- 반드시 존댓말을 사용하세요 (예: ~하셨네요, ~했습니다, ~해보세요, ~이에요).
- 친근하고 따뜻하게 격려하는 톤을 유지하세요.
- '~했다', '~이다' 같은 반말/평서형은 절대 사용하지 마세요.

[작성 가이드라인]
1. summary: 이번 달 학습을 한 문장으로 요약해주세요. 구체적인 기술 키워드를 포함해주세요.
2. growthPoints: 사용자가 성장한 점을 2~3개 구체적으로 작성해주세요.
3. improvementPoints: 아쉬운 점이나 보완하면 좋을 점을 1~2개 작성해주세요. 없으면 격려 메시지를 넣어주세요.
4. nextMonthSuggestions: 다음 달 학습 방향을 2~3개 구체적으로 제안해주세요.

[출력 형식]
반드시 아래 형식의 JSON 데이터만 출력하세요 (마크다운 없이 순수 문자열만):
{
    "summary": "한 줄 요약",
    "growthPoints": ["성장 포인트 1", "성장 포인트 2"],
    "improvementPoints": ["개선 포인트 1"],
    "nextMonthSuggestions": ["추천 1", "추천 2"]
}
        """.trimIndent()
    }
}
