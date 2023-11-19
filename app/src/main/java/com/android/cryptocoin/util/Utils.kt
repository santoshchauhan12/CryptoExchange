package com.android.cryptocoin.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import retrofit2.Response

object Utils {
    fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
        val error = ApiErrorUtils.parseError(resp)
        return AppResult.Error(Exception(error.error_message))
    }

    fun <T : Any> handleSuccess(response: Response<T>): AppResult<T> {
        response.body()?.let {
            return AppResult.Success(it)
        } ?: return handleApiError(response)
    }

    @JvmStatic
    @BindingAdapter("imageDrawable")
    fun setBgViewColor(
        view: ImageView,
        id: Int?
    ) {
        id?.let { view.setImageDrawable(ContextCompat.getDrawable(view.context, it)) }
    }


    fun formatDoubleToOneDecimal(doubleValue: Double): String {
        return String.format("%.1f", doubleValue)
    }

    fun prependDollarUsingConcatenation(value: String): String {
        return "\$" + value
    }

    fun appendPercentUsingInterpolation(value: String): String {
        return "$value%"
    }
}