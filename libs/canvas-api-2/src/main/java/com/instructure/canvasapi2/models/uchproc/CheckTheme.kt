package com.instructure.canvasapi2.models.uchproc

import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CheckTheme(
        val lecture: Boolean = false,
        val practical: Boolean = false,
        val kmdro: Boolean = false
) : CanvasModel<CheckTheme>()


