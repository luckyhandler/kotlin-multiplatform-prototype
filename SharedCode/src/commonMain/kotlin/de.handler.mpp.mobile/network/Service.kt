package de.handler.mpp.mobile.network

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

@UnstableDefault
class Service {
    private val baseUrl = "http://api.open-notify.org/astros.json"

    private val client by lazy {
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json(JsonConfiguration(strictMode = false)))
            }
        }
    }

    suspend fun fetchPeople(): AstronautWrapper {
        return client.get(baseUrl)
    }
}