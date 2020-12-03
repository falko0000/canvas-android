package com.instructure.teacher.factory


import com.instructure.canvasapi2.models.uchproc.PointJournal
import com.instructure.teacher.presenters.WeekStudentsPointPresenter
import com.instructure.teacher.viewinterface.WeekStudentsPointView
import instructure.androidblueprint.PresenterFactory


class WeekStudentsPointPresenterFactory (
        private val courseId: Long,
        private val pointJournal: PointJournal
) : PresenterFactory<WeekStudentsPointView, WeekStudentsPointPresenter> {
    override fun create() = WeekStudentsPointPresenter(courseId, pointJournal)
}