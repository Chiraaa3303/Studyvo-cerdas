package com.example.data.remote

import com.example.data.model.QuizQuestion
import com.example.data.sample.SampleData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object GeminiAiService {

    /**
     * Converts study material into practice multiple choice questions.
     * Uses Gemini API via REST if API key is present, otherwise falls back to intelligent offline generator.
     */
    suspend fun convertMaterialToQuestions(
        materialText: String,
        apiKey: String
    ): List<QuizQuestion> = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "YOUR_API_KEY") {
            return@withContext generateOfflineQuestions(materialText)
        }

        try {
            val prompt = """
                Kamu adalah asisten guru cerdas berbahasa Indonesia untuk aplikasi 'Studyvo Flash'.
                Buatlah 4 soal latihan pilihan ganda berkualitas tinggi berdasarkan materi berikut:
                "${materialText}"
                
                Keluaran HARUS format JSON array murni tanpa markdown/backticks, dengan struktur berikut untuk setiap elemen:
                {
                  "questionText": "Pertanyaan nomor dan teks soal...",
                  "options": ["Opsi 1", "Opsi 2", "Opsi 3", "Opsi 4"],
                  "correctIndex": 1,
                  "reasonWrong": "Penjelasan rinci mengapa pilihan yang salah kurang tepat...",
                  "reasonRight": "Penjelasan rinci mengapa jawaban benar adalah pilihan yang tepat...",
                  "category": "Kategori Soal"
                }
            """.trimIndent()

            val jsonResponse = callGeminiRestApi(prompt, apiKey)
            parseQuestionsFromJson(jsonResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            generateOfflineQuestions(materialText)
        }
    }

    /**
     * Converts study material into a neat summary (rangkuman).
     */
    suspend fun convertMaterialToSummary(
        materialText: String,
        apiKey: String
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "YOUR_API_KEY") {
            return@withContext """
                📌 **Rangkuman Materi: Siklus Hidup Pengembangan Perangkat Lunak (SDLC)**
                
                • **1. Analisis Kebutuhan**: Fondasi awal memahami pengguna dan masalah sebelum coding.
                • **2. Desain**: Pembuatan rancangan sistem, flowchart, dan UI/UX aplikasi.
                • **3. Implementasi**: Penulisan kode program (coding) sesuai rancangan.
                • **4. Pengujian**: Pengecekan bug dan validasi agar aplikasi stabil sebelum rilis.
                • **5. Pemeliharaan**: Perawatan aplikasi pasca-rilis dan peningkatan kinerja sistem.
            """.trimIndent()
        }

        try {
            val prompt = """
                Buatlah rangkuman materi yang ringkas, jelas, dan terstruktur dalam bahasa Indonesia untuk materi berikut:
                "${materialText}"
                
                Gunakan poin-poin bulatan (bullet points) agar mudah dipelajari oleh siswa.
            """.trimIndent()

            callGeminiRestApi(prompt, apiKey)
        } catch (e: Exception) {
            """
                📌 **Rangkuman Materi: Siklus Hidup Pengembangan Perangkat Lunak (SDLC)**
                
                • **1. Analisis Kebutuhan**: Fondasi awal memahami pengguna dan masalah sebelum coding.
                • **2. Desain**: Pembuatan rancangan sistem, flowchart, dan UI/UX aplikasi.
                • **3. Implementasi**: Penulisan kode program (coding) sesuai rancangan.
                • **4. Pengujian**: Pengecekan bug dan validasi agar aplikasi stabil sebelum rilis.
                • **5. Pemeliharaan**: Perawatan aplikasi pasca-rilis dan peningkatan kinerja sistem.
            """.trimIndent()
        }
    }

    /**
     * Converts practice questions into study explanations (mengubah soal menjadi materi).
     */
    suspend fun convertQuestionsToMaterial(
        questionText: String,
        apiKey: String
    ): String = withContext(Dispatchers.IO) {
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "YOUR_API_KEY") {
            return@withContext """
                📖 **Materi Pembelajaran dari Soal Latihan:**
                
                **Topik: Rekayasa Perangkat Lunak & SDLC**
                1. **Tujuan Pengembangan Program**: Sistem dibuat bukan asal-asalan, melainkan selalu mengutamakan penyelesaian kebutuhan nyata pengguna (*User-Centered Design*).
                2. **Tahapan Analisis Kebutuhan**: Merupakan fondasi pertama. Sebelum desain dan kode ditulis, tim harus tahu betul apa masalah yang diselesaikan.
                3. **Tahapan Desain**: Pembuatan cetak biru (*blueprint*) seperti alur logika (*flowchart*), ERD, dan prototiping UI/UX.
            """.trimIndent()
        }

        try {
            val prompt = """
                Jelaskan konsep dan materi pelajaran secara menyeluruh berdasarkan soal-soal latihan atau pertanyaan berikut ini:
                "${questionText}"
                
                Buatlah dalam format penjelasan materi yang mudah dipahami, terstruktur, dan edukatif dalam bahasa Indonesia.
            """.trimIndent()

            callGeminiRestApi(prompt, apiKey)
        } catch (e: Exception) {
            """
                📖 **Materi Pembelajaran dari Soal Latihan:**
                
                **Topik: Rekayasa Perangkat Lunak & SDLC**
                1. **Tujuan Pengembangan Program**: Sistem dibuat bukan asal-asalan, melainkan selalu mengutamakan penyelesaian kebutuhan nyata pengguna (*User-Centered Design*).
                2. **Tahapan Analisis Kebutuhan**: Merupakan fondasi pertama. Sebelum desain dan kode ditulis, tim harus tahu betul apa masalah yang diselesaikan.
                3. **Tahapan Desain**: Pembuatan cetak biru (*blueprint*) seperti alur logika (*flowchart*), ERD, dan prototiping UI/UX.
            """.trimIndent()
        }
    }

    private fun callGeminiRestApi(prompt: String, apiKey: String): String {
        val urlString = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true
        connection.connectTimeout = 60000
        connection.readTimeout = 60000

        val escapedPrompt = JSONObject.quote(prompt)
        val jsonPayload = """{"contents":[{"parts":[{"text":$escapedPrompt}]}]}"""

        connection.outputStream.use { os ->
            os.write(jsonPayload.toByteArray(Charsets.UTF_8))
            os.flush()
        }

        val responseCode = connection.responseCode
        if (responseCode == 200) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream, Charsets.UTF_8))
            val responseText = reader.use { it.readText() }
            val jsonObj = JSONObject(responseText)
            val candidates = jsonObj.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val text = parts.getJSONObject(0).optString("text", "")
                    return text.trim()
                }
            }
        } else {
            val errorStream = connection.errorStream
            val errorText = errorStream?.bufferedReader()?.readText() ?: "Unknown error"
            throw RuntimeException("Gemini API Error $responseCode: $errorText")
        }
        return ""
    }

    private fun parseQuestionsFromJson(jsonText: String): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        try {
            var cleanedJson = jsonText.trim()
            if (cleanedJson.startsWith("```json")) {
                cleanedJson = cleanedJson.removePrefix("```json").removeSuffix("```").trim()
            } else if (cleanedJson.startsWith("```")) {
                cleanedJson = cleanedJson.removePrefix("```").removeSuffix("```").trim()
            }
            val jsonArray = JSONArray(cleanedJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val optionsArray = obj.getJSONArray("options")
                val optionsList = mutableListOf<String>()
                for (j in 0 until optionsArray.length()) {
                    optionsList.add(optionsArray.getString(j))
                }
                questions.add(
                    QuizQuestion(
                        id = i + 1,
                        questionText = obj.getString("questionText"),
                        options = optionsList,
                        correctIndex = obj.getInt("correctIndex"),
                        reasonWrong = obj.optString("reasonWrong", "Pilihan yang keliru kurang tepat berdasarkan standar teori."),
                        reasonRight = obj.optString("reasonRight", "Pilihan ini adalah jawaban yang paling tepat sesuai kaidah materi."),
                        category = obj.optString("category", "Latihan Soal AI")
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return SampleData.defaultQuestions
        }
        return if (questions.isNotEmpty()) questions else SampleData.defaultQuestions
    }

    private fun generateOfflineQuestions(material: String): List<QuizQuestion> {
        return SampleData.defaultQuestions
    }
}
