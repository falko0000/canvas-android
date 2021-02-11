package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeekPoints(
        @SerializedName("point")
        val totalPoints: Double = 0.0,
        @SerializedName("divided")
        val pointDivided: PointDivided,
        @SerializedName("week_number")
        val weekNumber: Int = 1
): CanvasModel<WeekPoints>()