package com.instructure.canvasapi2.models.uchproc

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointJournalGroup(
        @SerializedName("full_name")
        val fullName: String = "",
        @SerializedName("student_id")
        val studentId: Long = 0,
        @SerializedName("record_book")
        val recordBook: String = "",
        @SerializedName("absence_threshold")
        val absenceThreshold: Boolean = false,
        val rating: PointRetings? = null,
        @SerializedName("total_point")
        val totalPoint: Double = 0.0,
        @SerializedName("assessment_word")
        val assessmentWord: String? = "",
        val assessment: String? = "",
        @SerializedName("assessment_exact")
        val assessmentExact: Double = 0.0,
        val exam: Double = 0.0,
        @SerializedName("exam_fx")
        val fxExam: Double = 0.0,
        @SerializedName("exam_f")
        val fExam: Double =0.0,
        var avatarUrl: String? = null,
        var pronouns: String? = null,
        var selectedWeek: Int = -1
): CanvasModel<PointJournalGroup>(), Parcelable{
        override val id get() = studentId
}

