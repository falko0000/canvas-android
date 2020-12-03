package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointDivided(
        @SerializedName("lecture_att")
        val lectureAttendance: Double = 0.0,
        @SerializedName("practical_att")
        val practicalAttendance: Double = 0.0,
        @SerializedName("practical_act")
        val practicalActive: Double = 0.0,
        @SerializedName("KMDRO_att")
        val kmdroAttendance: Double = 0.0,
        @SerializedName("KMDRO_act")
        val kmdroActive: Double = 0.0,
        @SerializedName("KMD")
        val kmd: Double = 0.0,
) : CanvasModel<PointDivided>()