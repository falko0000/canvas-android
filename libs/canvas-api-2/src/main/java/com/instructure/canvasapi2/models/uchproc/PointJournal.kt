package com.instructure.canvasapi2.models.uchproc

import com.instructure.canvasapi2.models.CanvasModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PointJournal(
        val header: PointJournalSetting? = null,
        val points: ArrayList<PointJournalGroup> = ArrayList()
): CanvasModel<PointJournal>()