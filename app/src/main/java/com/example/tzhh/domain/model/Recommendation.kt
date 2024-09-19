package com.example.tzhh.domain.model

data class Recommendation(
    val id: String?,
    val title: String,
    val link: String,
    val button: ButtonText?
)

data class ButtonText(
    val text: String
)
