package com.android.cryptocoin.util

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Response
import java.io.IOException


object ApiErrorUtils {

    fun parseError(response: Response<*>): APIError {

        val gson = GsonBuilder().create()
        val error: APIError

        try {


            error = gson.fromJson(response.errorBody()?.string(), APIError::class.java)
            if(response.code() == 400) {
                error.error_message = "Invalid Request"
            } else if(response.code() == 403) {
                error.error_message = "Unauthorized access"

            } else if(response.code() == 500) {
                error.error_message = "Internal server error"
            }
        } catch (e: IOException) {
            e.message?.let { Log.d(TAG, it) }
            return APIError()
        }
        return error
    }

}