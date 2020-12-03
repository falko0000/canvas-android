package com.instructure.teacher.factory

import com.instructure.canvasapi2.models.CanvasContext
import com.instructure.teacher.presenters.PointJournalPresenter
import com.instructure.teacher.viewinterface.PointJournalView
import instructure.androidblueprint.PresenterFactory

class PointJournalPresenterFactory(private val mCanvasContext: CanvasContext) : PresenterFactory<PointJournalView, PointJournalPresenter> {
    override fun create() = PointJournalPresenter(mCanvasContext)
}