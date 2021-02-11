package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ratings(
        @SerializedName("first")
        val firstReting: ArrayList<RatingSetting> = ArrayList(),
        @SerializedName("second")
        val secondReting: ArrayList<RatingSetting> = ArrayList()
): CanvasModel<Ratings>()
