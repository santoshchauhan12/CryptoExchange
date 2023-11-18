package com.android.cryptocoin.model.bitcoin.logo

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MetaDataResponse(
    @Json(name = "data")
    val data: Map<String, CryptoItem>,
    @Json(name = "status")
    val status: Status
)

@JsonClass(generateAdapter = true)
data class CryptoItem(
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "logo")
    val logo: String,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "symbol")
    val symbol: String,
    @Json(name = "slug")
    val slug: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "date_added")
    val dateAdded: String,
    @Json(name = "date_launched")
    val dateLaunched: String,
    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "platform")
    val platform: Any?, // If it's always null, you can replace Any? with a more specific type
    @Json(name = "category")
    val category: String,
)

@JsonClass(generateAdapter = true)
data class Urls(
    @Json(name = "website")
    val website: List<String>,
    @Json(name = "technical_doc")
    val technicalDoc: List<String>,
    @Json(name = "twitter")
    val twitter: List<String>,
    @Json(name = "reddit")
    val reddit: List<String>,
    @Json(name = "message_board")
    val messageBoard: List<String>,
    @Json(name = "announcement")
    val announcement: List<String>,
    @Json(name = "chat")
    val chat: List<String>,
    @Json(name = "explorer")
    val explorer: List<String>,
    @Json(name = "source_code")
    val sourceCode: List<String>,
)

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "timestamp")
    val timestamp: String,
    @Json(name = "error_code")
    val errorCode: Int,
    @Json(name = "error_message")
    val errorMessage: String,
    @Json(name = "elapsed")
    val elapsed: Int,
    @Json(name = "credit_count")
    val creditCount: Int,
    @Json(name = "notice")
    val notice: String,
)
