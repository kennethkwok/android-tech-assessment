package com.pelagohealth.codingchallenge.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FactDTO(
    val id: String,
    val text: String?,
    val source: String?,

    @Json(name = "source_url")
    val sourceUrl: String?,

    val language: String?,
    val permalink: String?,
)