package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointWeeks(
    @SerializedName("weeks")
    val pointWeeks: ArrayList<WeekPoints>? = ArrayList()
): CanvasModel<PointWeeks>()