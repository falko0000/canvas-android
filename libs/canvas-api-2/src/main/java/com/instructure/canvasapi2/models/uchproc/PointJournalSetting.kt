package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointJournalSetting(
        @SerializedName("rating")
        val ratings: Ratings? = null,
        @SerializedName("max_week_point")
        val maxWeekPoint: Double? = 0.0,
        @SerializedName("max_lecture_att")
        val maxLectureAttendance: Double? = 0.0,
        @SerializedName("max_practical_att")
        val maxPracticalAttendance: Double? = 0.0,
        @SerializedName("max_practical_act")
        val maxPracticalActive: Double? = 0.0,
        @SerializedName("max_KMDRO_att")
        val maxKMDROAttendance: Double? = 0.0,
        @SerializedName("max_KMDRO_act")
        val maxKMDROActive: Double? = 0.0,
        @SerializedName("max_KMD")
        val maxKMD: Double? = 0.0
): CanvasModel<PointJournalSetting>()