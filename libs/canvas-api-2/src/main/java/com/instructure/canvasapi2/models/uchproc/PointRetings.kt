package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointRetings(
        @SerializedName("first")
        val firstRatingPoints: PointWeeks? = null,
        @SerializedName("second")
        val secondRatingPoints: PointWeeks? = null,
): CanvasModel<PointRetings>()