import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import shared.models.NewsResponse


class ApiClient {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { // Use ContentNegotiation plugin
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }
    }

    private val apiKey = "be46ef0efea24cd584947a3ecc84a6c6"
    private val baseUrl = "https://newsapi.org/v2/top-headlines?country=us&apiKey=$apiKey"

    suspend fun fetchNews(): NewsResponse {
        val response: HttpResponse = client.get(baseUrl)
        response.status
        return response.body() // Deserialize into NewsResponse
    }
}