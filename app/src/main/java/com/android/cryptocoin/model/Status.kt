package com.android.cryptocoin.model.bitcoin

import com.google.gson.annotations.SerializedName

data class Status(
    val timestamp: String,
    @SerializedName("error_code") val errorCode: Int,
    @SerializedName("error_message") val errorMessage: String,
    val elapsed: Int,
    @SerializedName("credit_count") val creditCount: Int
)