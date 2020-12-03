package com.instructure.teacher.viewinterface

import com.instructure.canvasapi2.models.uchproc.PointJournal

interface WeekStudentsPointView {
    fun onDataSet(pointJournal: PointJournal)
    fun onErrorSettingData()
}