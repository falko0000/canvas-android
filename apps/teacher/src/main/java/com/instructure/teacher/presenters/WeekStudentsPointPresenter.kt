package com.instructure.teacher.presenters



import com.instructure.canvasapi2.models.uchproc.PointJournal

import com.instructure.teacher.events.SubmissionUpdatedEvent

import com.instructure.teacher.viewinterface.WeekStudentsPointView
import instructure.androidblueprint.Presenter
import kotlinx.coroutines.Job
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WeekStudentsPointPresenter(
        private var courseId: Long,
        private var pointJournal: PointJournal,
) : Presenter<WeekStudentsPointView> {
    private var mView: WeekStudentsPointView? = null
    private var mApiJob: Job? = null

    fun setupData() {
        if(pointJournal != null) return


    }
    override fun onViewAttached(view: WeekStudentsPointView): Presenter<WeekStudentsPointView> {
        mView = view
        EventBus.getDefault().register(this)
        return this
    }

    override fun onViewDetached() {
        mView = null
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroyed() {
        mView = null
        mApiJob?.cancel()
    }
    @Suppress("unused")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun updateSubmission(event: SubmissionUpdatedEvent) {

    }


}