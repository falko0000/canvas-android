package com.instructure.canvasapi2.models.uchproc

import com.google.gson.annotations.SerializedName
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatingSetting(
        val number: Int = 0,
        @SerializedName("is_editable")
        val isEditable: Boolean = false,
        @SerializedName("attendance_permission")
        val checkTheme: CheckTheme? = null
): CanvasModel<RatingSetting>()